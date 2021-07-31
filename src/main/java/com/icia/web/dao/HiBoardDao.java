package com.icia.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;

@Repository("hiBoardDao")
public interface HiBoardDao 
{
	//게시물 등록
	public int boardInsert(HiBoard hiBoard);
	
	//게시물 첨부파일 등록
	public int boardFileInsert(HiBoardFile hiBoardFile);
	
	//게시물 총 수
	public long boardListCount(HiBoard hiBoard);
	
	//게시물 리스트
	public List<HiBoard> boardList(HiBoard hiBoard);
}
