open module JpaEnJavaFXEnTesten {
	
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	
	requires java.persistence;
	requires java.instrument;
	requires java.sql;
	requires org.junit.jupiter.api;
	requires mockito.junit.jupiter;
	requires org.mockito;
	requires org.junit.jupiter.params;
	requires java.desktop;
	requires java.mail;
	
	exports ui;
	exports main;
	exports domein;
	exports repository;
	exports controllers;
}