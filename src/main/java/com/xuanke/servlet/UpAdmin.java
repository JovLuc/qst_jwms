package com.xuanke.servlet;
/**
 * 更改管理员密码
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xuanke.util.DB;

/**
 * Servlet implementation class UpAdmin
 */
@WebServlet("/UpAdmin")
public class UpAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public UpAdmin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//更改密码操作
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//获取页面传过来的值
		int oldId = Integer.parseInt(request.getParameter("oldId"));//哪一个管理员的数据要更改
		String oldName = request.getParameter("oldName");
		String oldPw = request.getParameter("oldPw");
		String newPw = request.getParameter("newPw");
		
		//创建db对象
		DB db = new DB();
		//创建一个判断该管理员输入的旧密码是否一致的sql
		String sql = "select * from admin where userId='" + oldId + "' and userPw='" + oldPw + "'";
		//创建更新sql语句
		String sql1 = "update admin set userPw = ? where userId = ?";
		
		//创建一个rs结果集,ps对象
		PreparedStatement ps;
		ResultSet rs;
		//调用db中的查询方法
		ps = db.getPs(sql);
		try {
			rs = ps.executeQuery();//执行语句
			//判断旧密码是否存在,rs.next()返回的是布尔值
			if(rs.next()) {//存在,则执行更新操作
				//调用更新操作
				ps = db.getPs(sql1);
				//要更新的数据
				ps.setString(1, newPw);
				ps.setInt(2, oldId);
				ps.executeUpdate();//执行更新
				//成功返回管理员信息页面,这里可以设置一个提示页面再跳,不设置也可以
				PrintWriter out = response.getWriter();
				response.sendRedirect("admin/adminMes.jsp?oldName=" + oldName + "&oldId=" + oldId);
			}else {//不存在,则跳回修改页面,并提示错误
				PrintWriter out = response.getWriter();
				response.sendRedirect("admin/adminEdi.jsp?oldName=" + oldName + "&oldId=" + oldId + "&message=" + "1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		doGet(request, response);
	}

}
