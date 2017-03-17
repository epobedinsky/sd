package com.securediary.tag;

/**
 * Полная информаци о тэге (название и количество записей)
 *
 * @author Побединский Евгений
 *         12.06.14 16:34
 */
public class FullTag {
	private Long id;
	private String title;
	private Integer count;

	public FullTag(Long id, String title, Integer count) {
		this.id = id;
		this.title = title;
		this.count = count;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Integer getCount() {
		return count;
	}
}
