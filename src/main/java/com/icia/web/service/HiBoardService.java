package com.icia.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.icia.web.dao.HiBoardDao;
import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;

@Service("hiBoardService")
public class HiBoardService  
{
	private static Logger logger = LoggerFactory.getLogger(HiBoardService.class);
	
	//파일 저장 디렉토리
	@Value("#{env['upload.save.dir']}")
	private String UPLOAD_SAVE_DIR;
	
	@Autowired
	private HiBoardDao hiBoardDao;
	
	//게시물 등록
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int boardInsert(HiBoard hiBoard) throws Exception
	{
		int count = hiBoardDao.boardInsert(hiBoard);
		
		//게시물 정상 등록 되고, 첨부파일이 있다면 첨부파일 등로오오옥
		if(count > 0 && hiBoard.getHiBoardFile() != null)
		{
			HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
			
			hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
			hiBoardFile.setFileSeq((short)1);
			
			hiBoardDao.boardFileInsert(hiBoard.getHiBoardFile());
			
		}
		
		return count;
	}
	
	//총 게시물 수 
	public long boardListCount(HiBoard hiBoard)
	{
		
		long count = 0;
		
		try
		{
			count = hiBoardDao.boardListCount(hiBoard);
		}
		catch(Exception e)
		{
			logger.error("[HiBoardService] boardListCount Exception", e);
		}
		
		return count;
	}
	
	//게시물 리스트
	public List<HiBoard> boardList(HiBoard hiBoard)
	{
		List<HiBoard> list = null;
		
		try
		{
			list = hiBoardDao.boardList(hiBoard);
		}
		catch(Exception e)
		{
			logger.error("[HiBoardService] boardList Exception", e);
		}
		
		return list;
	}
}






