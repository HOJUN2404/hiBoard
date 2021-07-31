package com.icia.web.controller;

import org.slf4j.LoggerFactory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.icia.common.model.FileData;
import com.icia.common.util.StringUtil;
import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;
import com.icia.web.model.Paging;
import com.icia.web.model.Response;
import com.icia.web.model.User;
import com.icia.web.service.HiBoardService;
import com.icia.web.service.UserService;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;


@Controller("hiBoardController")
public class HiBoardController 
{
	private static Logger logger = LoggerFactory.getLogger(HiBoardController.class);
	
	@Value("#{env['auth.cookie.name']}")
	private String AUTH_COOKIE_NAME;
	
	@Value("#{env['upload.save.dir']}")
	private String UPLOAD_SAVE_DIR;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HiBoardService hiBoardService;
	
	private static final int LIST_COUNT = 5;		//한 페이지의 게시물 수 
	private static final int PAGE_COUNT = 5;		//페이징 수
	
	@RequestMapping(value="/board/list")
	public String list(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		//조회 항목(1:작성자조회, 2:제목조회, 3:내용조회)
		String searchType = HttpUtil.get(request, "searchType");
		//조회 값
		String searchValue = HttpUtil.get(request, "searchValue", "");
		//현재 페이지 
		long curPage = HttpUtil.get(request, "curPage", (long)1);
		//총 게시물 수
		long totalCount = 0;
		//게시물 리스트
		List<HiBoard> list = null;
		//조회 객체
		HiBoard search = new HiBoard();
		//페이징 객체
		Paging paging = null;	
		
		if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchType))
		{
			search.setSearchType(searchType);
			search.setSearchValue(searchValue);
		}
		else
		{
			searchType = "";
			searchValue = "";
		}
		
		totalCount = hiBoardService.boardListCount(search);
		
		logger.debug("totalCount : " + totalCount);
		
		if(totalCount > 0)
		{
			paging = new Paging("/board/list", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			
			paging.addParam("searchType", searchType);
			paging.addParam("searchValue", searchValue);
			paging.addParam("curPage", curPage);
			
			search.setStartRow(paging.getStartRow());
			search.setEndRow(paging.getEndRow());
			
			list = hiBoardService.boardList(search);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		
		return "/board/list";
	}
	
	//게시물 등록 폼
	@RequestMapping(value="/board/writeForm")
	public String writeForm(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		//쿠키 값
		String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		//조회 항목(1:작성자 조회, 2:제목 조회, 3:내용 조회)
		String searchType = HttpUtil.get(request, "searchType", "");
		//조회 값 
		String searchValue = HttpUtil.get(request, "searchValue", "");
		//현재 페이지
		long curPage = HttpUtil.get(request, "curPage", (long)1);
		
		//사용자정보 조회
		User user = userService.userSelect(cookieUserId);
		
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("user", user);
		
		return "/board/writeForm";
	}
	
	//게시물 등록 시작한다잉
	@RequestMapping(value="/board/writeProc", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> writeProc(MultipartHttpServletRequest request, HttpServletResponse response)
	{
		String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		String hiBbsTitle = HttpUtil.get(request, "hiBbsTitle", "");
		String hiBbsContent = HttpUtil.get(request, "hiBbsContent", "");
		FileData fileData = HttpUtil.getFile(request, "hiBbsFile", UPLOAD_SAVE_DIR);
		
		Response<Object> ajaxResponse = new Response<Object>();
		
		if(!StringUtil.isEmpty(hiBbsTitle) && !StringUtil.isEmpty(hiBbsContent))
		{
			HiBoard hiBoard = new HiBoard();
			
			hiBoard.setUserId(cookieUserId);
			hiBoard.setHiBbsTitle(hiBbsTitle);
			hiBoard.setHiBbsContent(hiBbsContent);
			
			if(fileData != null && fileData.getFileSize() > 0)
			{
				HiBoardFile hiBoardFile = new HiBoardFile();
				
				hiBoardFile.setFileName(fileData.getFileName());
				hiBoardFile.setFileOrgName(fileData.getFileOrgName());
				hiBoardFile.setFileExt(fileData.getFileExt());
				hiBoardFile.setFileSize(fileData.getFileSize());
				
				hiBoard.setHiBoardFile(hiBoardFile);
			}
			
			try
			{
				if(hiBoardService.boardInsert(hiBoard) > 0)
				{
					ajaxResponse.setResponse(0, "Success");
				}
				else
				{
					ajaxResponse.setResponse(500, "Internal Server Error");
				}
			}
			catch(Exception e)
			{
				logger.error("[HiBoardController]/board/writeProc Exception", e);
				ajaxResponse.setResponse(500, "Internal Server Error");
			}
			
		}
		else
		{
			ajaxResponse.setResponse(400, "Bad Request");
		}
		
		logger.debug("[HiBoardController] /board/writeProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
		
		return ajaxResponse;
	}	
}		








