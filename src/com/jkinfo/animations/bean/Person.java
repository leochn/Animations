package com.jkinfo.animations.bean;

import com.jkinfo.animations.utils.PinyinUtils;

public class Person implements Comparable<Person> {
	private String name;
	private String pinyin;
	public Person(String name) {
		super();
		this.name = name;
		this.pinyin = PinyinUtils.getPinyin(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	// 要使Person能进行排序,需要实现Comparable<Person>接口,
	// 并实现compareTo()方法
	@Override
	public int compareTo(Person another) {
		return this.pinyin.compareTo(another.getPinyin());
	}
}
