package io.renren.modules.corporation.service.impl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.corporation.service.CorporationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("CorporationService")
public class CorporationServiceImpl implements CorporationService {

    @Autowired
    private DaoSupport daoSupport;
    @Autowired
    private CommService commService;

    @Override
    public Integer getCorId(PageData pageData) {
        return null;
    }

    @Override
    public List<PageData> getList(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("CorporationDao.corporationlistPage", page);
    }

    @Override
    public List<PageData> selectByCorId(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("CorporationDao.selectByCorId", pageData);
    }

    @Override
    @Transactional
    public void apply(PageData pageData) throws Exception {
        //插入社团表
        daoSupport.save("CorporationDao.add", pageData);
        //插入社团用户表
        daoSupport.save("CorporationDao.addCorUser", pageData);
    }

    @Override
    @Transactional
    public void updatefile(PageData pageData, MultipartFile qqCodeFile, HttpServletRequest request,String type) throws Exception {
        Integer fileId = commService.addFile2DB(pageData);
        PageData pageData1 = new PageData();
        pageData1.put(type,fileId);
        pageData1.put("corid",pageData.getValueOfString("corid"));
        updateCor(pageData1);
    }

    @Override
    @Transactional
    public void add(PageData pageData) throws Exception {
        //插入文件表
        daoSupport.save("FileDao.add", pageData);
        //插入社团表
        daoSupport.save("CorporationDao.add", pageData);
    }

    @Override
    public void delCor(PageData pageData) throws Exception {
        daoSupport.update("CorporationDao.update", pageData);
    }

    @Override
    public void updateCor(PageData pageData) throws Exception {
        daoSupport.update("CorporationDao.updateCor", pageData);
    }
}
