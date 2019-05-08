package io.renren.commBusiness.entity;

import io.renren.common.entity.PageData;
import io.renren.common.util.Const;
import io.renren.common.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页类
 * @author FH QQ 313596790[青苔]
 * 创建时间：2014年6月28日
 */
public class Page {
	
	private int pageSize; //每页显示记录数
	private int totalPage;		//总页数
	private int totalCount;	//总记录数
	private int currPage;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	//private String pageStr;		//最终页面显示的底部翻页导航，详细见：getPageStr();
	private PageData data = new PageData();
	private List<PageData> list = new ArrayList<>();



	public Page(){
		try {
			this.pageSize = Integer.parseInt(Tools.readTxtFile(Const.PAGE));
		} catch (Exception e) {
			this.pageSize = 15;
		}
	}

	public int getTotalPage() {
		if(totalCount%pageSize==0)
			totalPage = totalCount/pageSize;
		else
			totalPage = totalCount/pageSize+1;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrPage() {
		if(currPage<=0)
			currPage = 1;
		if(currPage>getTotalPage())
			currPage = getTotalPage();
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	//拼接分页 页面及JS函数
//	public String getPageStr() {
//		StringBuffer sb = new StringBuffer();
//		if(totalResult>0){
//			sb.append("	<ul class=\"pagination pull-right no-margin\">\n");
//			if(currPage==1){
//				sb.append("	<li><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
//				sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
//				sb.append("	<li><a>首页</a></li>\n");
//				sb.append("	<li><a>上页</a></li>\n");
//			}else{
//				sb.append("	<li><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
//				sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(1)\">首页</a></li>\n");
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+(currentPage-1)+")\">上页</a></li>\n");
//			}
//			int showTag = 5;//分页标签显示数量
//			int startTag = 1;
//			if(currPage>showTag){
//				startTag = currPage-1;
//			}
//			int endTag = startTag+showTag-1;
//			for(int i=startTag; i<=totalPage && i<=endTag; i++){
//				if(currPage==i)
//					sb.append("<li class=\"active\"><a><font color='white'>"+i+"</font></a></li>\n");
//				else
//					sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+i+")\">"+i+"</a></li>\n");
//			}
//			if(currPage==totalPage){
//				sb.append("	<li><a>下页</a></li>\n");
//				sb.append("	<li><a>尾页</a></li>\n");
//			}else{
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+(currentPage+1)+")\">下页</a></li>\n");
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+totalPage+")\">尾页</a></li>\n");
//			}
//			sb.append("	<li><a>共"+totalPage+"页</a></li>\n");
//			sb.append("	<li><select title='显示条数' style=\"width:55px;float:left;margin-top:1px;\" onchange=\"changeCount(this.value)\">\n");
//			sb.append("	<option value='"+showCount+"'>"+showCount+"</option>\n");
//			sb.append("	<option value='10'>10</option>\n");
//			sb.append("	<option value='20'>20</option>\n");
//			sb.append("	<option value='30'>30</option>\n");
//			sb.append("	<option value='40'>40</option>\n");
//			sb.append("	<option value='50'>50</option>\n");
//			sb.append("	<option value='60'>60</option>\n");
//			sb.append("	<option value='70'>70</option>\n");
//			sb.append("	<option value='80'>80</option>\n");
//			sb.append("	<option value='90'>90</option>\n");
//			sb.append("	<option value='99'>99</option>\n");
//			sb.append("	</select>\n");
//			sb.append("	</li>\n");
//
//			sb.append("</ul>\n");
//			sb.append("<script type=\"text/javascript\">\n");
//
//			//换页函数
//			sb.append("function nextPage(page){");
//			sb.append(" top.jzts();");
//			sb.append("	if(true && document.forms[0]){\n");
//			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
//			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
//			sb.append("		document.forms[0].action = url;\n");
//			sb.append("		document.forms[0].submit();\n");
//			sb.append("	}else{\n");
//			sb.append("		var url = document.location+'';\n");
//			sb.append("		if(url.indexOf('?')>-1){\n");
//			sb.append("			if(url.indexOf('currentPage')>-1){\n");
//			sb.append("				var reg = /currentPage=\\d*/g;\n");
//			sb.append("				url = url.replace(reg,'currentPage=');\n");
//			sb.append("			}else{\n");
//			sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
//			sb.append("			}\n");
//			sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
//			sb.append("		document.location = url;\n");
//			sb.append("	}\n");
//			sb.append("}\n");
//
//			//调整每页显示条数
//			sb.append("function changeCount(value){");
//			sb.append(" top.jzts();");
//			sb.append("	if(true && document.forms[0]){\n");
//			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
//			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + \"1&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
//			sb.append("		document.forms[0].action = url;\n");
//			sb.append("		document.forms[0].submit();\n");
//			sb.append("	}else{\n");
//			sb.append("		var url = document.location+'';\n");
//			sb.append("		if(url.indexOf('?')>-1){\n");
//			sb.append("			if(url.indexOf('currentPage')>-1){\n");
//			sb.append("				var reg = /currentPage=\\d*/g;\n");
//			sb.append("				url = url.replace(reg,'currentPage=');\n");
//			sb.append("			}else{\n");
//			sb.append("				url += \"1&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
//			sb.append("			}\n");
//			sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + \"&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
//			sb.append("		document.location = url;\n");
//			sb.append("	}\n");
//			sb.append("}\n");
//
//			//跳转函数
//			sb.append("function toTZ(){");
//			sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
//			sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
//			sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
//			sb.append("nextPage(toPaggeVlue);");
//			sb.append("}\n");
//			sb.append("</script>\n");
//		}
//		pageStr = sb.toString();
//		return pageStr;
//	}

//	public void setPageStr(String pageStr) {
//		this.pageStr = pageStr;
//	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {

		this.pageSize = pageSize;
	}

	public int getCurrentResult() {
		currentResult = (getCurrPage()-1)*getPageSize();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}

	public PageData getData() {
		return data;
	}

	public void setData(PageData data) {
		this.data = data;
	}

	public List<PageData> getList() {
		return list;
	}

	public void setList(List<PageData> list) {
		this.list = list;
	}
}
