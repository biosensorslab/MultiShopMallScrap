package com.prism.Product_Finder;

import java.util.ArrayList;
import java.util.List;



public class Online_mall {
	public static void main(String[] args) throws InterruptedException {


		List<String> scap_address = new ArrayList<String>();
		System.out.println("Start main method.");

		GMarket_LinkHandler linkHandler = new GMarket_LinkHandler();
		linkHandler.run();

//		Amazone_LinkHandler amazone_linkHandler = new Amazone_LinkHandler();
//		amazone_linkHandler.run();
		System.out.println("Multi thread Data scrap finished");
	}
}
