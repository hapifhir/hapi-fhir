<th:block th:fragment="changelog(from, to)">

# Changelog

<th:block th:each="version : ${changelog.getVersions(from, to)}">

# HAPI FHIR <th:block th:text="${version}"/> <th:block th:if="${changelog.changes.get(version).codename} != null" th:text="' (' + ${changelog.changes.get(version).codename} + ')'"/> <th:block th:if="${changelog.changes.get(version).releaseDate} == null">(Prerelease)</th:block>

## Release Information

<th:block th:if="${changelog.changes.get(version).releaseDate} == null">
Note: This version of HAPI FHIR is a SNAPSHOT (prerelease), meaning that it has
not yet been released, but all changes and fixes listed here are available to try
out as <a href="/hapi-fhir/docs/getting_started/downloading_and_importing.html#snapshot">Snapshot Builds</a>.
</th:block>
<th:block th:if="${changelog.changes.get(version).releaseDate} != null">
**Released:** <th:block th:text="${changelog.changes.get(version).releaseDate.getValueAsString()}"/>
</th:block>
<th:block th:if="${changelog.changes.get(version).codename} != null">
**Codename:** <th:block th:text="'(' + ${changelog.changes.get(version).codename} + ')'"/>
</th:block>

<th:block th:if="${changelog.changes.get(version).upgrade} != null">
## Upgrade Instructions

[(${changelog.changes.get(version).upgrade})]
</th:block>

## Changes

<table class="table">
    <tr th:each="change : ${changelog.changes.get(version)}">
        <td>
            <a th:name="'change' + ${version} + '-' + ${change.id}"></a>
            <span style="color: #129c49; font-size: 1.1em;" th:if="${change.type} == 'add'">
                <i class="fa fa-plus"></i>
            </span>
            <span style="color: #129c49; font-size: 1.1em;" th:if="${change.type} == 'change'">
                <i class="fa fa-cogs"></i>
            </span>
            <span style="color: #ee2324; font-size: 1.1em;" th:if="${change.type} == 'fix'">
                <i class="fa fa-bug"></i>
            </span>
            <span style="color: #64c2d1; font-size: 1.1em;" th:if="${change.type} == 'perf'">
                <i class="fa fa-rocket"></i>
            </span>
            <span style="color: #ee2324; font-size: 1.1em;" th:if="${change.type} == 'security'">
                <i class="fa fa-shield-alt"></i>
            </span>
        </td>
        <td>
            <a th:if="${change.issue != null}" th:href="'https://github.com/hapifhir/hapi-fhir/issues/' + ${change.issue}" th:text="'#' + ${change.issue}"></a>
        </td>
        <td>
            <th:block th:utext="${change.title}"/>
        </td>
    </tr>
</table>

</th:block>

</th:block>
