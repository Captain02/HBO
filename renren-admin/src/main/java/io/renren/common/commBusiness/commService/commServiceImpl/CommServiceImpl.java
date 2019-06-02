package io.renren.common.commBusiness.commService.commServiceImpl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateTool;
import io.renren.common.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
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

    //文件上传路径
    @Value("${fileUploudPath}")
    public String FILEUPLOUD;
//    @Value("${HBO.basedir}")
//    String basedir;

    @Override
    public List<PageData> getselectes(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("commDao.selectComm",pageData);
    }
    @Override
    public String uploadFile(MultipartFile picture, HttpServletRequest request,String RelativePath){
        /*request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+request.getContextPath();*/
        //savepath为访问路径
        StringBuffer savePath= new StringBuffer();
//        savePath.append(System.getProperty("user.dir")).append("/file").append(RelativePath);
        savePath.append(FILEUPLOUD).append(RelativePath);



        StringBuffer visitPath = new StringBuffer();
//        visitPath.append(request.getScheme()).append("://").append(request.getServerName())
//                .append(":").append(request.getServerPort()).append(request.getContextPath())
//                .append(RelativePath);


//        String path = request.getServletContext().getRealPath(RelativePath);
        //文件路径
//        StringBuffer stringBuffer = new StringBuffer();
        //拼接文件名
        String date = DateTool.dateToStringYYHHDD(new Date());
        visitPath.append(RelativePath).append(date+picture.getOriginalFilename());
//        savepath.append(date).append(picture.getOriginalFilename());
//        visitPath.append(date+picture.getOriginalFilename());
        File file1 = new File(savePath.toString(),date+picture.getOriginalFilename());
        System.out.println("visitPath+++++++++++++++++++++++++++++"+visitPath);
        System.out.println("savePath++++++++++++++++++++++++++++++"+savePath);
        //创建文件夹
        if (!file1.getParentFile().exists()){
            file1.getParentFile().mkdirs();
        }
        try {
            //上传文件
            picture.transferTo(file1);
            return visitPath.toString();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //文件删除
    @Override
    public boolean deleteFile(String filenameAndPath){
        File delFile = new File(filenameAndPath);
        if(delFile.isFile() && delFile.exists()) {
            delFile.delete();
            return true;
        }else {
            return false;
        }
    }

    //保存文件到数据库
    public Integer addFile2DB(PageData pageData) throws Exception {
        pageData = (PageData) daoSupport.save("FileDao.add",pageData);
        return pageData.getValueOfInteger("id");
    }

}
