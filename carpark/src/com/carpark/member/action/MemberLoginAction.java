package com.carpark.member.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carpark.action.Action;
import com.carpark.member.model.MemberDto;
import com.carpark.member.model.service.MemberServiceImpl;

public class MemberLoginAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		HttpSession session = request.getSession();
		
		MemberDto memberDto = new MemberDto();
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		memberDto.setUser_id(id);
		memberDto.setUser_pass(pass);
		MemberServiceImpl.getMemberService().login(id,pass);
		
		session.setAttribute("memberInfo",memberDto);
		
		return memberDto==null ? "/member/loginOk.jsp":"/member/loginFail.jsp";
	}

}