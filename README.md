基于POI的excel导入中间件<br>
excel转list集合<br>
基于注解形式的单元格验证，并且可以自由扩展自己的单元格验证规则<br>
![image]https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p3.png<br>
注解的使用范围<br>
![image]https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p2.png<br>
<br>
注解的使用示例，继承自BaseRowEntity，会附加isSuccess、message两个字段，用于填充转换、检验结果<br>
![image]https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p4.png<br>
excel模板测试数据<br>
![image]https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p1.png<br>
<br>
**使用步骤**(已包含在test代码里)<br>
1.实例化工具对象<br>
```Java
ExcelWorksheet worksheet = new ExcelWorksheet(TestClass.class, "template.xls");
```
构造函数有三个重载方法
```Java
public ExcelWorksheet(Class<T> t, String pFilePath)
public ExcelWorksheet(Class<T> t, String pFilePath, int pSheetIndex)
public ExcelWorksheet(Class<T> t, String pFilePath, int pSheetIndex, int pTitleRowIndex)
```
<br>
2.1.1转换成自定义的对象<br>
```Java
List<TestClass> list = worksheet.transform();
```
2.1.2转换成自定义对象并增加转换规则<br>
```Java
list = worksheet.transform(m -> {
	TestClass entity = ((TestClass) m);
	entity.column1 = entity.column1 + "__postfix";
});
```
2.2.1转换成自定义的对象并验证单元格数据<br>
```Java
List<TestClass> list2 = worksheet.check();
```
2.2.2转换成自定义的对象并验证单元格数据+自定义验证规则<br>
```Java
worksheet.getCell("column3").addChecker(event -> {
	ServiceResult res = new ServiceResult();
	if (StringUtils.isNotBlank(event.getValue())) {
		Row row = event.getRow();
		ExcelCell c4 = event.getCell("column4");
		row.getCell(c4.getIndex()).setCellType(CellType.STRING);
		if (row.getCell(c4.getIndex()).getStringCellValue().equals(event.getValue())) {
			res.failed(String.format("[%s]不能等于[%s]", event.getCell().getTitle(),c4.getTitle()));
		}
	}
	return res;
});
list2 = worksheet.check();
```
2.2.3转换成自定义的对象+增加转换规则+验证单元格数据+自定义验证规则<br>
list2 = worksheet.check(m -> {
	TestClass entity = ((TestClass) m);
	entity.column1 = entity.column1 + "__postfix";
});
<br>
**联系方式**<br>
QQ 373119611