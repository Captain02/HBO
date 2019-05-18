package io.renren.common.dao;

import java.io.Serializable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class Excel implements Serializable{
    private String headTextName; // 列头（标题）名
    private String propertyName; // 对应字段名
    private Integer cols; // 合并单元格数
    private XSSFCellStyle cellStyle;

    public Excel() {
    }

    public Excel(String headTextName, String propertyName) {
        this.headTextName = headTextName;
        this.propertyName = propertyName;
    }

    public Excel(String headTextName, String propertyName, Integer cols) {
        super();
        this.headTextName = headTextName;
        this.propertyName = propertyName;
        this.cols = cols;
    }

    public String getHeadTextName() {
        return headTextName;
    }

    public void setHeadTextName(String headTextName) {
        this.headTextName = headTextName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public XSSFCellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(XSSFCellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    @Override
    public String toString() {
        return "Excel [headTextName=" + headTextName + ", propertyName=" + propertyName + ", cols=" + cols
                + ", cellStyle=" + cellStyle + "]";
    }
}
