package com.damoim.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.damoim.model.vo.Member;
import com.damoim.service.EmailService;
import com.damoim.service.MemberService;
import com.damoim.service.MembershipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
public class MemberController {
	int count = 0;
	@Autowired
	private MemberService service;
	
	@Autowired
	private MembershipService infoService;
	
    @Autowired
    private EmailService emailService;
	
	
	// 로그인 , 해당 회원 정보 , 가입 클럽 코드 및 등급을 세션에
		@ResponseBody
		@PostMapping("/login")
		public boolean login(Member member, HttpServletRequest request, Model model) {
			System.out.println("????");
			HttpSession session = request.getSession();
			// 로그인 성공 !
			if (service.login(member) != null) {
				
				session.setAttribute("mem", service.login(member)); 
				// 로그인 정보 세션에
				// 내가 가입한 클럽 정보 체크용
		
				// 해당 id를 가진 맴버의 맴버쉽 의 모든정보 + 맴버, 등급 등등
				System.out.println(infoService.grade(member));
				session.setAttribute("membership", infoService.grade(member));
				return true;
				// 로그인 실패!
			}
				return false;			
		}
	
	// *** 회원가입 관련
		
	// 회원가입 관련 아이디 중복 체크용
	@ResponseBody
	@PostMapping("/idCheck")
	public boolean idCheck(Member member) {
		Member mem = service.idCheck(member);
		return mem == null;
		
	}
	
	@ResponseBody
	@PostMapping("/nicknameCheck") // 회원가입시 닉네임 중복 체크
	public boolean nicknameCheck(Member member) {
		Member mem = service.nicknameCheck(member);
		return mem == null;
			
	}
	@PostMapping("/signUp") // 회원가입 메서드
	public String signUp(Member member ,String addrDetail ) {
		

		Member mem = member;
		String addr = mem.getAddr();
		System.out.println("일반주소 : " + addr);
		System.out.println("상세 주소 : " + addrDetail);
		addr += "#"+ addrDetail;
		System.out.println("합친 주소 #이 구분자 : " + addr);
		mem.setAddr(addr);
		service.signUp(member);	
		System.out.println(member);
		return "redirect:/";
		
	}
	@GetMapping("/logout") // 로그아웃 메서드
    public String logout(HttpServletRequest request) {
		System.out.println("logout!!!!");
		//HttpSession session = request.getSession();
		//session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/dummyUpdate")
	public String dummyUpdate() {
		service.dummyUpdate();
		return "redirect:/";
	}
	
	@GetMapping("/myMembership") // 내가 가입한 클럽확인
	public String myMembership(Member member, Model model) {
		
		// 내 등급별 클럽
		model.addAttribute("membership", infoService.grade(member));
		
		return "mypage/myMembership";
	}
	
	// 비밀번호 확인후 update 페이지 이동
	@PostMapping("/updateCheck")
	public String updateCheck(Member vo, HttpServletRequest request, String pwdCheck) {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("mem");
		vo.setId(member.getId());
		vo.setPwd(member.getPwd()); 
		service.updateCheck(member); 
		
		if (vo.getPwd().equals(pwdCheck)) {
			System.out.println();
			return "/mypage/update";
		} else {
			return "redirect:/updateCheck";
		}
	}

	@PostMapping("/update")
	public String update(Member vo, Model model, HttpServletRequest request, String addrDetail) {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("mem");
		vo.setId(member.getId());
		vo.setAddr(member.getAddr());
		String addr = member.getAddr();
		String addr1[] = addr.split("#");
		String addr2 = Arrays.toString(addr1);
		System.out.println(addr2);
		// #구분자로 주소, 상세주소로 쪼개짐
		
		model.addAttribute("splitAddr", addr2);
		
//		for (String a : beforeAddr) {
//			System.out.println("#구분자 기준으로 나누어진 주소 : " + a);
//			String addr1 = a;
//			String addr2 = a;
//			System.out.println(addr1 + addr2);
//		}
			
//		System.out.println("현재 가지고있는 주소 : " + beforeAddr);
		
		if (addrDetail == "") {
			vo.setAddr(member.getAddr());
		} else {
			vo.setAddr(member.getAddr() + "#" + addrDetail);
		}
		
		session.setAttribute("mem", vo);
		service.addrUpdate(vo);
		service.update(vo);
		
		return "/mypage/update";
	}



   @PostMapping("/sendEmail")
   public String sendEmail(@RequestParam("id") String id, @RequestParam("email") String email, Model model) {
    	    Member member = new Member();
    	    member.setId(id);
    	    member.setEmail(email);
    	    System.out.println("DB에 보낼 정보 : " + member);
    	    Member mem = emailService.memberEmailIdcheck(member);
    	    System.out.println("DB의 정보 : " + mem);
        try {
        	System.out.println("서비스 진입전 member 정보 : " + mem);
            emailService.processPasswordReset(mem);
            System.out.println("서비스 진입성공");
            model.addAttribute("message", "임시 비밀번호가 이메일로 전송되었습니다.");
            System.out.println("비밀번호 변경 완료");
        } catch (Exception e) {
            model.addAttribute("message", "비밀번호 재설정에 실패했습니다.");
            System.out.println("비밀번호 변경 실패");
        }

        return "redirect:/"; // 인덱스 페이지로 리다이렉트
    }
}
	
		
	
	
	
	// 해야할것!
	// 1. # 구분자로 합쳐진 주소 값을 다시 나눠서 
	// 		addr 과 addrDetail에 따로 입력
	

	
	
	
	
	/*
	// kakao로그인 요청을 처리한다.
	@PostMapping("/kakao-login")
	public String loginWithKakao(KakaoLoginForm form){
		log.info("카카오 로그인 인증정보:"+ form);
		
		User user = User.builder()
					.email(form.getEmail())
					.name(form.getName())
					.img(form.getImg())
					.loginType(KAKAO_LOGIN_TYPE)
					.build();
		
		User savedUser = userService.loginWithKakao(user);
		
		// 저장된 회원정보가 없으면 전달받은 회원정보를 세션에 저장, 있으면 기존 정보 저장.
		if(savedUser != null) {
			SessionUtils.addAttribute("LOGIN_USER", savedUser);
		}else {
			SessionUtils.addAttribute("LOGIN_USER", user);
		}
		
		return "redirect:/";
	}
	*/
	
	

}
	




