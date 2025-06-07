package com.zpark.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {
	public static void main(String[] args) {
		// 准备哪些表做代码生成
		List<String> tables = new ArrayList<>();
//		tables.add("user");
		tables.add("collection");
//		tables.add("house");
//		tables.add("appointment");
//		tables.add("house");
//		tables.add("education");
//		tables.add("experience");
//		tables.add("honor");
//		tables.add("project");
//		tables.add("recommend");
//		tables.add("training");
//		tables.add("member");

		FastAutoGenerator.create("jdbc:mysql://107.173.181.247/springboot?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true",
						"root","lhj445")
				.globalConfig(builder -> {
					builder.author("XMC")               //作者
							.outputDir(
									System.getProperty("user.dir")+
											"\\House-FindHouse\\src\\main\\java")    //输出路径(写到java目录)
							//.enableSwagger()           //开启swagger
							.commentDate("yyyy-MM-dd");
//							.fileOverride();          //开启覆盖之前生成的文件

				})
				.packageConfig(builder -> {
					builder.parent("com.zpark") // 指导生成到父包的位置
							//.moduleName("practice") // 设置父包模块名
							.entity("pojo") // 包配置
							.service("service")
							.serviceImpl("service.impl")
							.controller("controller")
							.mapper("mapper")
							.xml("mapper")
							.pathInfo(Collections.singletonMap(OutputFile.mapper,
									System.getProperty("user.dir")+"" +
											"\\House-FindHouse\\src\\main\\resources\\com\\zpark\\mapper"));
				})
				.strategyConfig(builder -> {
					builder.addInclude(tables) // 设置需要生成的表名
							//.addTablePrefix("sys_") // 过滤表名前缀，如：sys_user，最终生成的类名只需要user
							.serviceBuilder() // service策略配置
							.formatServiceFileName("I%sService") // Service接口类名，%s适配，根据表名替换
							.formatServiceImplFileName("%sServiceImpl") // Service层接口实现类类名，%s适配，根据表名替换
							.entityBuilder()
							.enableLombok() // 开启lombok
							//.logicDeleteColumnName("deleted") // 说明逻辑删除是那一个字段，没有就将这段注释掉
							.enableTableFieldAnnotation() // 属性加上注解说明
							.controllerBuilder() // controller策略配置
							// 映射路径使用连字符格式，而不是驼峰
							.enableHyphenStyle()
							.formatFileName("%sController") // 控制器类名，%s适配，根据表名替换
							.enableRestStyle() // 开启@RestController
							.mapperBuilder() // mapper策略配置
							//生成通用的resultMap
							.enableBaseResultMap()
							.superClass(BaseMapper.class) // 继承那个父类
							.formatMapperFileName("I%sMapper")
							// .enableMapperAnnotation() // @Mapper注解开启，如果不配置，需要在启动类上加@MapperScans
							.formatXmlFileName("I%sMapper"); // xml名字
				})
				/*.templateConfig(new Consumer<TemplateConfig.Builder>() {
					@Override
					public void accept(TemplateConfig.Builder builder) {
						// 实体类使用我们自定义模板
						builder.entity("templates/myentity.java");
					}
				})*/
				//.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
}
