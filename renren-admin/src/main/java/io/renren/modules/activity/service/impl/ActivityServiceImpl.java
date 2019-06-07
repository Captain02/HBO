package io.renren.modules.activity.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> activityListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.activitylistPage", page);
    }

    @Override
    @Transactional
    public void add(PageData pageData) throws Exception {
        //插入活动
        daoSupport.save("ActivityDao.add",pageData);
        //插入活动的进程
        String[] processNodes = pageData.getValueOfString("processNodes").split(",");
        for (int i = 0; i <processNodes.length ; i++) {
            pageData.put("processNode",processNodes[i]);
            daoSupport.save("ActprocessDao.add",pageData);
        }
        String croWdPeopleStr = pageData.getValueOfString("croWdPeople");
        String[] split = croWdPeopleStr.split(",");
        List<String> list = Arrays.asList(split);
        pageData.put("list",list);
        daoSupport.save("ActivityDao.addActCroWdPeople", pageData);
    }

    @Override
    public PageData getActivityById(PageData pageData) throws Exception {
        return (PageData) daoSupport.findForObject("ActivityDao.getActivityById",pageData);
    }

    @Override
    public void updateAct(PageData pageData) throws Exception {
        //更新活动进程表
        if(pageData.containsKey("processNodes") && pageData.containsKey("status")){
            String[] processNodes = pageData.getValueOfString("processNodes").split(",");
            String[] status = pageData.getValueOfString("status").split(",");
            if(status.length != processNodes.length){
                throw new Exception("进程节点和状态不一致");
            }
            for (int i = 0; i < processNodes.length; i++) {
                pageData.put("processNode",processNodes[i]);
                pageData.put("status",Integer.parseInt(status[i]));
                daoSupport.update("ActprocessDao.update",pageData);
            }
        }
        //更新活动表
        daoSupport.update("ActivityDao.updateAct",pageData);
    }

    @Override
    public List<PageData> getReplies(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.getReplieslistPage",page);
    }
}