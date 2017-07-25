package com.github.pasp.tool;

import com.github.pasp.tool.coder.Coder;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			showHelp();
			return;
		}
		String[] param = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			param[i - 1] = args[i];
		}
		switch (args[0]) {
		case "coder":
			new Coder().code(param);
			break;
		default:
			showHelp();
		}
	}

	private static void showHelp() {
		System.out.println("用法：");
		System.out.println("tools [command] [options]");
		System.out.println("支持的命令：");
		System.out.println("coder \t代码生成");
		System.out.println("\t选项：");
		System.out.println("\t-clean \t清空输出目录");
		System.out.println("\t-output xxx  \t指定输出目录，默认为当前目录output");
		System.out.println("\t-config xxx  \t指定配置文件，默认为当前目录config.xml");
		System.out.println("\t例如：coder -clean -output D:/output -config D:/config.xml");
	}

}
