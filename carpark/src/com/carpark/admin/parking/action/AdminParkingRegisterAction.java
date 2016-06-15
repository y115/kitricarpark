package com.carpark.admin.parking.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carpark.action.Action;
import com.carpark.admin.model.service.AdminParkingServiceImpl;
import com.carpark.common.model.ParkingDetailDto;
import com.carpark.member.model.MemberDto;
import com.carpark.member.model.service.CommonServiceImpl;
import com.carpark.member.model.service.MemberParkingServiceImpl;
import com.carpark.util.Encoder;
import com.carpark.util.NumberCheck;
import com.carpark.util.PageNavigator;
import com.carpark.util.StringCheck;

public class AdminParkingRegisterAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String visit = StringCheck.nullToBlank(request.getParameter("visit"));
		int pg = NumberCheck.nullToOne(request.getParameter("pg"));
		String key = StringCheck.nullToBlank(request.getParameter("key"));
		String word = StringCheck.nullToBlank(request.getParameter("word"));
		String flag = request.getParameter("flag");
		
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo");
		
		if(memberDto != null) {
			String ownerId = memberDto.getUser_id();
			
			int parkingId = CommonServiceImpl.getCommonService().getNextParkingId();
			
			ParkingDetailDto parkingDto = new ParkingDetailDto();
			
			parkingDto.setPark_id(parkingId);
			parkingDto.setPark_public(Integer.parseInt(request.getParameter("park_public")));
			parkingDto.setPark_name(request.getParameter("parkName"));
			
			String coordinate = request.getParameter("coordinate");//지도에서 가져온 좌표
			if(coordinate != null && !coordinate.isEmpty()) {
				StringTokenizer st = new StringTokenizer(coordinate, ",");//lat, lng로 나눔
				String latitude = st.nextToken().substring(1);// ( 제거 
				String longitude = st.nextToken().replace(")", "").trim();// ) 제거
				
				parkingDto.setLatitude(Double.parseDouble(latitude));//dto에 넣기
				parkingDto.setLongitude(Double.parseDouble(longitude));
			}
			
			parkingDto.setOwner_id(ownerId);
			
			parkingDto.setPay_yn(StringCheck.nullToBlank(request.getParameter("payYn")));
			parkingDto.setSatur_pay_yn(StringCheck.nullToBlank(request.getParameter("saturPayYn")));
			parkingDto.setHoli_pay_yn(StringCheck.nullToBlank(request.getParameter("holiPayYn")));
			parkingDto.setPark_capacity(NumberCheck.nullToOne(request.getParameter("parkCapacity")));
			parkingDto.setPark_time_rate(NumberCheck.nullToOne(request.getParameter("parkTimeRate")));
			parkingDto.setPark_rate(NumberCheck.nullToOne(request.getParameter("parkRate")));
			parkingDto.setAdd_park_rate(NumberCheck.nullToOne(request.getParameter("addParkRate")));
			parkingDto.setDay_max_pay(NumberCheck.nullToOne(request.getParameter("dayMaxPay")));
			parkingDto.setFulltime_monthly_pay(NumberCheck.nullToOne(request.getParameter("fullTimeMonthlyPay")));
			parkingDto.setPark_flag(NumberCheck.nullToOne(request.getParameter("parkFlag")));
			parkingDto.setContent(StringCheck.nullToBlank(request.getParameter("content").replace("\r\n", "<br>")));
			parkingDto.setDetailAddr(StringCheck.nullToBlank(request.getParameter("parkAddress") + " " + request.getParameter("parkDetailAddress")));
			parkingDto.setFacility(StringCheck.nullToBlank(request.getParameter("facility")));
			parkingDto.setPark_avgPoint(3.0);
			parkingDto.setPark_visit(0);
			
			AdminParkingServiceImpl.getAdminParkingService().parkingRegister(parkingDto);
			
			List<ParkingDetailDto> list = AdminParkingServiceImpl.getAdminParkingService().parkingList(1, "", "0", key, word);
			request.setAttribute("parkingList", list);

			PageNavigator navigator = CommonServiceImpl.getCommonService().getPageNavigatorAdminParking(pg, flag, visit, key, word);
			navigator.setRoot(request.getContextPath());
			Map<String, String> map = new HashMap<String, String>();
			String javascript = "adminParkList";
			map.put("javascript", javascript);
			map.put("pg", pg + "");
			map.put("flag", flag);
			map.put("visit", visit);
			navigator.setNavigator(map);
			request.setAttribute("navigator", navigator);

			return "/admin/parking/list.jsp";
		} else
			return "/member/loginFail.jsp";
		}

}
