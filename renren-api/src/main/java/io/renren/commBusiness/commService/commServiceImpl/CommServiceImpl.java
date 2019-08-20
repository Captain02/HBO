package io.renren.commBusiness.commService.commServiceImpl;

import io.renren.commBusiness.commService.CommService;
import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CommServiceImpl implements CommService {

    @Autowired
    DaoSupport daoSupport;
    //文件上传路径
    @Value("${fileUploudPath}")
    public String FILEUPLOUD;

    @Override
    public List<PageData> getselectes(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("commDao.selectComm", pageData);
    }

    @Override
    public String uploadFile(MultipartFile picture, HttpServletRequest request, String RelativePath) {
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
    public boolean deleteFile(String filenameAndPath) {
        File delFile = new File(filenameAndPath);
        if (delFile.isFile() && delFile.exists()) {
            delFile.delete();
            return true;
        } else {
            return false;
        }
    }

    //保存文件到数据库
    public Integer addFile2DB(PageData pageData) throws Exception {
        daoSupport.save("FileDao.add", pageData);
        return pageData.getValueOfInteger("id");
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
