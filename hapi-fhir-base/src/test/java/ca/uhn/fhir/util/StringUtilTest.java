package ca.uhn.fhir.util;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringUtilTest {

	@Test
	public void testLeft() {
		assertNull(StringUtil.left(null, 1));
		assertEquals("", StringUtil.left("", 10));
		assertEquals("STR", StringUtil.left("STR", 10));
		assertEquals(".", StringUtil.left("...", 1));

		// check supplementary chars
		assertEquals("\uD800\uDF01", StringUtil.left("\uD800\uDF01\uD800\uDF02", 1));
	}

	@Test
	public void testNormalizeString() {
		assertEquals("TEST TEST", StringUtil.normalizeStringForSearchIndexing("TEST teSt"));
		assertEquals("AEIØU", StringUtil.normalizeStringForSearchIndexing("åéîøü"));
		assertEquals("杨浩", StringUtil.normalizeStringForSearchIndexing("杨浩"));
		assertEquals(null, StringUtil.normalizeStringForSearchIndexing(null));
	}

	@Test
	public void testToStringNoBom() {
		String input = "help i'm a bug";
		String output = StringUtil.toUtf8String(input.getBytes(Charsets.UTF_8));
		assertEquals(input, output);
	}

	@Test
	public void testToStringWithBom() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(bos, StandardCharsets.UTF_8));
		out.write('\ufeff');
		out.write("help i'm a bug");
		out.close();

		byte[] bytes = bos.toByteArray();
		String output = StringUtil.toUtf8String(bytes);
		assertEquals("help i'm a bug", output);
	}


	@Test
	public void testChompCharacter() {
		assertEquals(null, StringUtil.chompCharacter(null, '/'));
		assertEquals("", StringUtil.chompCharacter("", '/'));
		assertEquals("", StringUtil.chompCharacter("/", '/'));
		assertEquals("a", StringUtil.chompCharacter("a/", '/'));
		assertEquals("a/a", StringUtil.chompCharacter("a/a/", '/'));
		assertEquals("a/a", StringUtil.chompCharacter("a/a////", '/'));
	}
	
	@Test
	public void testTruncToMax()  {
		
		assertEquals(null, StringUtil.truncToMax(null, ";", 10));
		assertEquals("1234567890", StringUtil.truncToMax("1234567890", ";", 10));
		assertEquals(null, StringUtil.truncToMax("12345678901", ";", 10));
		assertEquals("12345", StringUtil.truncToMax("12345;678901", ";", 10));
		assertEquals("12345", StringUtil.truncToMax("12345;678901;abc", ";", 10));
		assertEquals("12345;6789", StringUtil.truncToMax("12345;6789;abc", ";", 10));

		assertEquals(null, StringUtil.truncToMax("12345;6789;abc", null, 10));
		assertEquals(null, StringUtil.truncToMax("12345;6789;abc", ";", 0));
		
		String org   = "Ab;自身抗体 CSF CSF;脊髓液;Cerebral spinal fluid;脑脊髓液 HTLV 1+2 Ab HTLV 1+2 Ab 区带模式 HTLV 1+2 型 Ab HTLV 1+2 型 Ab 区带模式 HTLV 1+2 型抗体 HTLV 1+2 型抗体区带模式 HTLV I+II 型 Ab HTLV I+II 型 Ab 区带模式 HTLV I+II 型抗体 HTLV I+II 型抗体区带模式 HTLV1+2 Ab HTLV1+2 Ab 区带模式 HTLV1+2 抗体 HTLV1+2 抗体区带模式 Human T-lymphotropic virus Type I;HTLV-1;Adult T-cell lymphoma virus type 1;HTLV 1+2 型;HTLV I+II 型;HTLV 1+2;HTLV1+2;人类 T 细胞白血病病毒 1+2 型;人类 T 细胞白血病病毒 I+II 型;人类 T-细胞白血病病毒 1+2 型;人类 T-细胞白血病病毒 I+II 型;人类嗜 T 淋巴细胞病毒 1+2 型;人类嗜 T 淋巴细胞病毒 I+II 型;人类嗜 T-淋巴细胞病毒 1+2 型;人类嗜 Human T-lymphotropic virus;Adult T-cell lymphoma virus;人类 T 细胞白血病病毒;人类 T-细胞白血病病毒;人类嗜 T 淋巴细胞病毒;人类嗜 T-淋巴细胞病毒;嗜人类 T 淋巴细胞病毒 I 型 人类 T 细胞白血病病毒 人类 T 细胞白血病病毒 1+2 型 Ab 人类 T 细胞白血病病毒 1+2 型 Ab 区带模式 人类 T 细胞白血病病毒 1+2 型抗体 人类 T 细胞白血病病毒 1+2 型抗体区带模式 人类 T 细胞白血病病毒 I+II 型 Ab 人类 T 细胞白血病病毒 I+II 型 Ab 区带模式 人类 T 细胞白血病病毒 I+II 型抗体 人类 T 细胞白血病病毒 I+II 型抗体区带模式 人类 T-细胞白血病病毒 人类 T-细胞白血病病毒 1+2 型 Ab 人类 T-细胞白血病病毒 1+2 型 Ab 区带模式 人类 T-细胞白血病病毒 1+2 型抗体 人类 T-细胞白血病病毒 1+2 型抗体区带模式 人类 T-细胞白血病病毒 I+II 型 Ab 人类 T-细胞白血病病毒 I+II 型 Ab 区带模式 人类 T-细胞白血病病毒 I+II 型抗体 人类 T-细胞白血病病毒 I+II 型抗体区带模式 人类 T-细胞白血病病毒（Human T-cell Leukemia Virus，HTLV） 人类嗜 T 淋巴细胞病毒 人类嗜 T 淋巴细胞病毒 1+2 型 Ab 人类嗜 T 淋巴细胞病毒 1+2 型 Ab 区带模式 人类嗜 T 淋巴细胞病毒 1+2 型抗体 人类嗜 T 淋巴细胞病毒 1+2 型抗体区带模式 人类嗜 T 淋巴细胞病毒 I+II 型 Ab 人类嗜 T 淋巴细胞病毒 I+II 型 Ab 区带模式 人类嗜 T 淋巴细胞病毒 I+II 型抗体 人类嗜 T 淋巴细胞病毒 I+II 型抗体区带模式 人类嗜 T-淋巴细胞病毒 人类嗜 T-淋巴细胞病毒 1+2 型 Ab 人类嗜 T-淋巴细胞病毒 1+2 型 Ab 区带模式 人类嗜 T-淋巴细胞病毒 1+2 型抗体 人类嗜 T-淋巴细胞病毒 1+2 型抗体区带模式 人类嗜 T-淋巴细胞病毒 I+II 型 Ab 人类嗜 T-淋巴细胞病毒 I+II 型 Ab 区带模式 人类嗜 T-淋巴细胞病毒 I+II 型抗体 人类嗜 T-淋巴细胞病毒 I+II 型抗体区带模式 人类嗜 T-淋巴细胞病毒（Human T-cell Lymphotrophic Virus，HTLV） 免疫印迹;西部印迹法;Immunoblot;immunoblotting;IB 分类型应答;分类型结果;名义性;名称型;名词型;名词性;标称性;没有自然次序的名义型或分类型应答 区带 区带（带型、条带）模式（模式特征、特征模式、图谱、式样、组份、组分、成员） 印象是一种诊断陈述，始终是对其他某种观察指标的解释或抽象（一系列检验项目结果、一幅图像或者整个某位病人），而且几乎总是由某位专业人员产生。;检查印象;检查印象/解释;检查的印象/解释;检查解释;解释;阐释 嗜人类 T 淋巴细胞病毒 嗜人类 T 淋巴细胞病毒 1+2 型 Ab 嗜人类 T 淋巴细胞病毒 1+2 型 Ab 区带模式 嗜人类 T 淋巴细胞病毒 1+2 型抗体 嗜人类 T 淋巴细胞病毒 1+2 型抗体区带模式 嗜人类 T 淋巴细胞病毒 I+II 型 Ab 嗜人类 T 淋巴细胞病毒 I+II 型 Ab 区带模式 嗜人类 T 淋巴细胞病毒 I+II 型抗体 嗜人类 T 淋巴细胞病毒 I+II 型抗体区带模式 嗜人类 T-淋巴细胞病毒 嗜人类 T-淋巴细胞病毒 1+2 型 Ab 嗜人类 T-淋巴细胞病毒 1+2 型 Ab 区带模式 嗜人类 T-淋巴细胞病毒 1+2 型抗体 嗜人类 T-淋巴细胞病毒 1+2 型抗体区带模式 嗜人类 T-淋巴细胞病毒 I+II 型 Ab 嗜人类 T-淋巴细胞病毒 I+II 型 Ab 区带模式 嗜人类 T-淋巴细胞病毒 I+II 型抗体 嗜人类 T-淋巴细胞病毒 I+II 型抗体区带模式 带 带状 带状核 微生物学;微生物学试验;微生物学试验（培养、DNA、抗原及抗体） 抗体带型;抗体条带模式;Ab区带模式;Ab带型;Ab条带模式（模式特征、特征模式、图谱、式样、组份、组分、成员） 时刻;随机;随意;瞬间 杆 杆状核 脊液 脊髓液 脑脊液（Cerebral spinal fluid，CSF） 脑脊髓液";
		String trunc = "Ab;自身抗体 CSF CSF;脊髓液;Cerebral spinal fluid;脑脊髓液 HTLV 1+2 Ab HTLV 1+2 Ab 区带模式 HTLV 1+2 型 Ab HTLV 1+2 型 Ab 区带模式 HTLV 1+2 型抗体 HTLV 1+2 型抗体区带模式 HTLV I+II 型 Ab HTLV I+II 型 Ab 区带模式 HTLV I+II 型抗体 HTLV I+II 型抗体区带模式 HTLV1+2 Ab HTLV1+2 Ab 区带模式 HTLV1+2 抗体 HTLV1+2 抗体区带模式 Human T-lymphotropic virus Type I;HTLV-1;Adult T-cell lymphoma virus type 1;HTLV 1+2 型;HTLV I+II 型;HTLV 1+2;HTLV1+2;人类 T 细胞白血病病毒 1+2 型;人类 T 细胞白血病病毒 I+II 型;人类 T-细胞白血病病毒 1+2 型;人类 T-细胞白血病病毒 I+II 型;人类嗜 T 淋巴细胞病毒 1+2 型;人类嗜 T 淋巴细胞病毒 I+II 型;人类嗜 T-淋巴细胞病毒 1+2 型;人类嗜 Human T-lymphotropic virus;Adult T-cell lymphoma virus;人类 T 细胞白血病病毒;人类 T-细胞白血病病毒;人类嗜 T 淋巴细胞病毒;人类嗜 T-淋巴细胞病毒;嗜人类 T 淋巴细胞病毒 I 型 人类 T 细胞白血病病毒 人类 T 细胞白血病病毒 1+2 型 Ab 人类 T 细胞白血病病毒 1+2 型 Ab 区带模式 人类 T 细胞白血病病毒 1+2 型抗体 人类 T 细胞白血病病毒 1+2 型抗体区带模式 人类 T 细胞白血病病毒 I+II 型 Ab 人类 T 细胞白血病病毒 I+II 型 Ab 区带模式 人类 T 细胞白血病病毒 I+II 型抗体 人类 T 细胞白血病病毒 I+II 型抗体区带模式 人类 T-细胞白血病病毒 人类 T-细胞白血病病毒 1+2 型 Ab 人类 T-细胞白血病病毒 1+2 型 Ab 区带模式 人类 T-细胞白血病病毒 1+2 型抗体 人类 T-细胞白血病病毒 1+2 型抗体区带模式 人类 T-细胞白血病病毒 I+II 型 Ab 人类 T-细胞白血病病毒 I+II 型 Ab 区带模式 人类 T-细胞白血病病毒 I+II 型抗体 人类 T-细胞白血病病毒 I+II 型抗体区带模式 人类 T-细胞白血病病毒（Human T-cell Leukemia Virus，HTLV） 人类嗜 T 淋巴细胞病毒 人类嗜 T 淋巴细胞病毒 1+2 型 Ab 人类嗜 T 淋巴细胞病毒 1+2 型 Ab 区带模式 人类嗜 T 淋巴细胞病毒 1+2 型抗体 人类嗜 T 淋巴细胞病毒 1+2 型抗体区带模式 人类嗜 T 淋巴细胞病毒 I+II 型 Ab 人类嗜 T 淋巴细胞病毒 I+II 型 Ab 区带模式 人类嗜 T 淋巴细胞病毒 I+II 型抗体 人类嗜 T 淋巴细胞病毒 I+II 型抗体区带模式 人类嗜 T-淋巴细胞病毒 人类嗜 T-淋巴细胞病毒 1+2 型 Ab 人类嗜 T-淋巴细胞病毒 1+2 型 Ab 区带模式 人类嗜 T-淋巴细胞病毒 1+2 型抗体 人类嗜 T-淋巴细胞病毒 1+2 型抗体区带模式 人类嗜 T-淋巴细胞病毒 I+II 型 Ab 人类嗜 T-淋巴细胞病毒 I+II 型 Ab 区带模式 人类嗜 T-淋巴细胞病毒 I+II 型抗体 人类嗜 T-淋巴细胞病毒 I+II 型抗体区带模式 人类嗜 T-淋巴细胞病毒（Human T-cell Lymphotrophic Virus，HTLV） 免疫印迹;西部印迹法;Immunoblot;immunoblotting;IB 分类型应答;分类型结果;名义性;名称型;名词型;名词性;标称性;没有自然次序的名义型或分类型应答 区带 区带（带型、条带）模式（模式特征、特征模式、图谱、式样、组份、组分、成员） 印象是一种诊断陈述，始终是对其他某种观察指标的解释或抽象（一系列检验项目结果、一幅图像或者整个某位病人），而且几乎总是由某位专业人员产生。;检查印象;检查印象/解释;检查的印象/解释;检查解释;解释";
		assertEquals(trunc, StringUtil.truncToMax(org, ";", 2000));
	}

}
