package io.renren.modules.activity.service.impl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateTool;
import io.renren.modules.activity.Entity.ActState;
import io.renren.modules.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private DaoSupport daoSupport;
    @Autowired
    private CommService commService;

    @Override
    public List<PageData> activityListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.activitylistPage", page);
    }

    @Override
    @Transactional
    public void add(PageData pageData,MultipartFile enclosure,HttpServletRequest request) throws Exception {

        //保存二维码路径
        pageData.put("fileName", DateTool.dateToStringYYHHDD(new Date()) + pageData.get("actName").toString() + ".jpg");
        pageData.put("filePath", "/file/QrCode/Activity/" + pageData.getValueOfString("fileName"));
        Integer fileId = commService.addFile2DB(pageData);
        if (fileId != null) {
            pageData.put("fileId", fileId);
        }
        //上传附件
        if (enclosure != null && !enclosure.isEmpty()) {
            if (!this.upload(pageData, enclosure, "/file/Activity/enclosure/", request)) {
                throw new Exception("文件上传失败");
            }
            Integer enclosureid = commService.addFile2DB(pageData);
            if (enclosureid != null) {
                pageData.put("enclosureid", enclosureid);
            }
        }



        //插入活动
        daoSupport.save("ActivityDao.add",pageData);
        //插入活动的进程
        String[] processNodes = pageData.getValueOfString("processNodes").split(",");
        for (int i = 0; i <processNodes.length ; i++) {
            pageData.put("processNode",processNodes[i]);
            daoSupport.save("ActprocessDao.add",pageData);
        }
        String croWdPeopleStr = pageData.getValueOfString("croWdPeople");
        if (croWdPeopleStr.equals("")||croWdPeopleStr.equals("null")){
            croWdPeopleStr = "127";
        }
        String[] split = croWdPeopleStr.split(",");
        List<String> list = Arrays.asList(split);
        pageData.put("list",list);
        daoSupport.save("ActivityDao.addActCroWdPeople", pageData);
    }


    private Boolean upload(PageData pageData, MultipartFile file, String path, HttpServletRequest request) {
        String filePath = commService.uploadFile(file, request, path);
        if (filePath == null) {
            return false;
        }
        pageData.put("filePath", filePath);
        pageData.put("fileName", file.getOriginalFilename());
        return true;
    }

    @Override
    public PageData getActivityById(PageData pageData) throws Exception {
        return (PageData) daoSupport.findForObject("ActivityDao.getActivityById",pageData);
    }

    @Transactional
    @Override
    public void updateAct(PageData pageData,MultipartFile enclosure,HttpServletRequest request) throws Exception {
        daoSupport.delete("ActprocessDao.delByActId",pageData);
        String[] processNodes = pageData.getValueOfString("processNodes").split(",");
        for (int i = 0; i <processNodes.length ; i++) {
            pageData.put("processNode",processNodes[i]);
            daoSupport.save("ActprocessDao.add",pageData);
        }
        daoSupport.delete("ActivityDao.delActCroWdPeopleByActId",pageData);
        String croWdPeopleStr = pageData.getValueOfString("croWdPeople");
        String[] split = croWdPeopleStr.split(",");
        List<String> list = Arrays.asList(split);
        pageData.put("list",list);
        daoSupport.save("ActivityDao.addActCroWdPeople", pageData);
        //上传附件
        if (enclosure != null && !enclosure.isEmpty()) {
            if (!this.upload(pageData, enclosure, "/file/Activity/enclosure/", request)) {
                throw new Exception("文件上传失败");
            }
            Integer enclosureid = commService.addFile2DB(pageData);
            if (enclosureid != null) {
                pageData.put("enclosureid", enclosureid);
            }
        }

        daoSupport.update("ActivityDao.updateAct",pageData);

    }

    @Override
    public List<PageData> getReplies(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.getReplieslistPage",page);
    }

    @Override
    public void changeProcess(List<ActState> list) throws Exception {
        PageData pageData = new PageData();
        for (ActState actState : list) {
            pageData.put("actState",actState);
            daoSupport.update("ActprocessDao.changeProcess",pageData);
        }
    }

    @Override
    public List<PageData> getUserByActIdlistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.getUserByActIdlistPage",page);
    }

    @Override
    public void isLike(PageData pageData) throws Exception {
        if (pageData.getValueOfLong("type") == 1){
            daoSupport.delete("ActivityDao.delLike",pageData);
            daoSupport.save("ActivityDao.saveLike",pageData);
        }else {
            daoSupport.delete("ActivityDao.delLike",pageData);
        }
    }

    @Override
    @Transactional
    public void isCollection(PageData pageData) throws Exception {
        if (pageData.getValueOfLong("type") == 1){
            daoSupport.delete("ActivityDao.delCollection",pageData);
            daoSupport.save("ActivityDao.saveCollection",pageData);
        }else {
            daoSupport.delete("ActivityDao.delCollection",pageData);
        }
    }

    @Override
    public List<PageData> getActCharts(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.getActCharts",pageData);
    }

    @Override
    public void replies(PageData pageData) throws Exception {
        daoSupport.save("ActivityDao.replies",pageData);
    }

    @Transactional
    @Override
    public void repliesLike(PageData pageData) throws Exception {
        daoSupport.save("ActivityDao.repliesLike",pageData);
    }

    @Override
    public void delRepliesLike(PageData pageData) throws Exception {
        daoSupport.delete("ActivityDao.delRepliesLike",pageData);
    }

    @Override
    public List<PageData> getActivityUserId(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.getActivityUserId",pageData);
    }

    @Transactional
    @Override
    public void uploadqqcodeFile(PageData pageData) throws Exception {
        daoSupport.save("QqCodeFileDao.save",pageData);
        pageData.put("qqfileId",pageData.getValueOfInteger("id"));
        daoSupport.update("ActivityDao.updateAct",pageData);
    }


}