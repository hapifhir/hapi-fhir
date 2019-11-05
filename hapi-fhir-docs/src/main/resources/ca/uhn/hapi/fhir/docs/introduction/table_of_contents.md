# Table of Contents

Welcome to HAPI FHIR!

---

<table class="helpTocTable">
<th:block th:each="chapter : ${chapters}">
<tr class="helpTocChapter">
<td th:text="${chapter.sectionNumber} + '.0.0'" class="helpTocChapter"></td>
<td th:text="${chapter.title}" class="helpTocChapter"></td>
</tr>
<th:block th:each="page : ${chapter.pages}">
<tr>
<td th:text="${page.sectionNumber} + '.0'"></td>
<td class="helpTocPage"><a th:href="${page.link}" th:text="${page.title}"></a></td>
</tr>
<th:block th:each="section : ${page.sections}">
<tr>
<td th:text="${section.sectionNumber}"></td>
<td class="helpTocSection"><a th:href="${page.link} + ${section.anchor}" th:text="${section.title}"></a></td>
</tr>
</th:block>
</th:block>
</th:block>
</table>
