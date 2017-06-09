package com.prism.Product_Finder;

import java.util.ArrayList;
import java.util.List;



public class KoreaMall {
	public static void main(String[] args) throws InterruptedException {


		List<String> scap_address = new ArrayList<String>();
		System.out.println("Start main method.");
		G_Market_LinkHandler linkHandler = new G_Market_LinkHandler();
		linkHandler.run();
		System.out.println("Multi thread Data scrap finished");
	}
}
