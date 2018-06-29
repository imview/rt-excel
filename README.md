**基于POI的excel导入中间件**<br>
多种自由组合方式，帮你轻松实现数据转换和校验<br>
1.excel转换成自定义对象集合<br>
2.excel转换成自定义对象集合+自定义转换规则<br>
3.excel转换成自定义对象集合+自定义转换规则+单元格数据通用验证规则<br>
4.excel转换成自定义对象集合+自定义转换规则+单元格数据通用验证规则+自定义验证规则<br>
5.excel转换成自定义对象集合+单元格数据通用验证规则<br>
6.excel转换成自定义对象集合+单元格数据通用验证规则+自定义验证规则<br>
<br>
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p3.png)<br>
**注解的使用范围**<br>
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p2.png)<br>
<br>
**注解的使用示例**，继承自BaseRowEntity，会附加isSuccess、message两个字段，用于填充转换、检验结果<br>
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p4.png)<br>
**excel模板测试数据**<br>
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p1.png)<br>
<br>
**使用步骤**<br>
**1.实例化对象**
```Java
ExcelWorksheet worksheet = new ExcelWorksheet(TestClass.class, "template.xls");
```
构造函数有三个重载方法
```Java
public ExcelWorksheet(Class<T> t, String pFilePath)
public ExcelWorksheet(Class<T> t, String pFilePath, int pSheetIndex)
public ExcelWorksheet(Class<T> t, String pFilePath, int pSheetIndex, int pTitleRowIndex)
```
**2.1.1 excel转换成自定义对象集合**
```Java
List<TestClass> list = worksheet.transform();
list.forEach(m -> System.out.println(m.toString()));
```
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p5.png)<br>
**2.1.2 excel转换成自定义对象集合+自定义转换规则**
```Java
list = worksheet.transform(m -> {
	TestClass entity = ((TestClass) m);
	entity.column1 = entity.column1==null?"":entity.column1 + "__postfix";
});
list.forEach(m -> System.out.println(m.toString()));
```
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p6.png)<br>
**2.2.1 excel转换成自定义对象集合+单元格数据通用验证规则**
```Java
List<TestClass> list2 = worksheet.check();
list2.forEach(m -> System.out.println(m.toString()));
```
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p7.png)<br>
**2.2.2 excel转换成自定义对象集合+单元格数据通用验证规则+自定义验证规则**
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
list2.forEach(m -> System.out.println(m.toString()));
```
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p8.png)<br>
**2.2.3 excel转换成自定义对象集合+自定义转换规则+单元格数据通用验证规则+自定义验证规则**
```Java
list2 = worksheet.check(m -> {
	TestClass entity = ((TestClass) m);
	entity.column1 = entity.column1 + "__postfix";
});
list2.forEach(m -> System.out.println(m.toString()));
```
![image](https://raw.githubusercontent.com/roytian1217/rt-excel/master/doc/p9.png)<br>
<br>
**联系方式**<br>
QQ 373119611
