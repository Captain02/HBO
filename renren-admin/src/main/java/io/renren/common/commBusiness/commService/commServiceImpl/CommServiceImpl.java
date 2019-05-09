package io.renren.common.commBusiness.commService.commServiceImpl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommServiceImpl implements CommService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> getselectes(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("commMpper.selectComm",pageData);
    }
    @Override
    public String uploadFile(MultipartFile picture, HttpServletRequest request,String path){

        //文件路径
        StringBuffer stringBuffer = new StringBuffer();

        //拼接文件名
        String date = DateTool.dateToStringYYHHDD(new Date());
        stringBuffer.append(path).append(date+picture.getOriginalFilename());
        File file = new File(path,date+picture.getOriginalFilename());

        //创建文件夹
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            //上传文件
            picture.transferTo(file);
            return stringBuffer.toString();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public PageData uploadFile(MultipartFile picture, HttpServletRequest request,PageData pageData) throws Exception {
//        //根据社团id查出对应的学院、社团名称
//        List<PageData> pageDataList = (List<PageData>) daoSupport.findForList("corporationDao.selectCor", pageData);
//        String corName = pageDataList.get(0).toString();
//        String corCollege  = pageDataList.get(1).toString();
//        //获取文件在服务器的储存位置
//        String path = request.getSession().getServletContext().getRealPath("/upload/corCollege/corName");
//        File filePath = new File(path);
//        System.out.println("文件的保存路径：" + path);
//        if (!filePath.exists() && !filePath.isDirectory()) {
//            System.out.println("目录不存在，创建目录:" + filePath);
//            filePath.mkdir();
//        }
//
//        //获取原始文件名称(包含格式)
//        String originalFileName = picture.getOriginalFilename();
//        System.out.println("原始文件名称：" + originalFileName);
//
//        //获取文件类型，以最后一个`.`为标识
//        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//        System.out.println("文件类型：" + type);
//        //获取文件名称（不包含格式）
//        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
//
//        //设置文件新名称: 当前时间+文件名称（不包含格式）
//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String date = sdf.format(d);
//        String fileName = date + name + "." + type;
//        System.out.println("新文件名称：" + fileName);
//
//        //在指定路径下创建一个文件
//        File targetFile = new File(path, fileName);
//        //封装返回信息
//        pageData.put("originalFileName",originalFileName);
//        pageData.put("fileName",fileName);
//        pageData.put("path",path);
//        return pageData;
//    }
}
