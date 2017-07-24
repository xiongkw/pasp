package com.github.pasp.data;

public class SimpleModel {

	private Object value;

	private Object value1;

	private Object value2;

	public SimpleModel(Object value) {
		super();
		this.value = value;
	}

	public SimpleModel(Object value, Object value1, Object value2) {
		super();
		this.value = value;
		this.value1 = value1;
		this.value2 = value2;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}

}
