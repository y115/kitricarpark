package com.carpark.member.message.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.carpark.action.Action;
import com.carpark.member.model.MemberDto;
import com.carpark.member.model.MessageDto;
import com.carpark.member.model.service.CommonServiceImpl;
import com.carpark.member.model.service.MemberMessageServiceImpl;
import com.carpark.util.NumberCheck;

public class MemberMessageWriteAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
		
		int seq = CommonServiceImpl.getCommonService().getNextSeq();
		
		MessageDto messageDto = new MessageDto();
		messageDto.setSeq(seq);
		//messageDto.setUser_id(memberDto.getUser_id())session에서 데이터 가져와야함
		messageDto.setSubject(request.getParameter("subject"));
		messageDto.setContent(request.getParameter("content"));
		messageDto.setBcode(NumberCheck.nullToZero(request.getParameter("bcode")));
		messageDto.setMseq(seq);
		messageDto.setReceiverId(request.getParameter("receiver_id"));
		messageDto.setMsgFlag(0);
		
		seq = MemberMessageServiceImpl.getMemberMessageService().writeArticle(messageDto);
		
		request.setAttribute("seq", seq);
		
		return seq == 0 ? "/message/writeFail.jsp" : "/message/writeOk.jsp";
	}

}