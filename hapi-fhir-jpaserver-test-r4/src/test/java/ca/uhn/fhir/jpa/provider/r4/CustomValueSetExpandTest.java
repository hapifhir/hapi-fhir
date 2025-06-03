package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomValueSetExpandTest extends BaseResourceProviderR4Test {

  @Autowired
  private FhirContext myFhirCtx;

  @Test
  public void testExpandAndPrint() throws Exception {
    // 1. 使用低階 API 直接載入 ICD-10-CM CodeSystem (改為2023版本)
    TermLoaderSvcImpl termLoaderSvc = new TermLoaderSvcImpl(myTerminologyDeferredStorageSvc, myTermCodeSystemStorageSvc);
    
    // 載入2023版本的ICD-10-CM檔案
    String filename = "icd10cm-tabular-2023.xml";  // 使用你的2023版本檔案
    
    String resource = ClasspathUtil.loadResource(filename);
    List<ITermLoaderSvc.FileDescriptor> descriptors = new ArrayList<>();
    descriptors.add(new ITermLoaderSvc.ByteArrayFileDescriptor(filename, resource.getBytes(StandardCharsets.UTF_8)));
    
    // 載入ICD-10-CM
    termLoaderSvc.loadIcd10cm(descriptors, new SystemRequestDetails());
    myTerminologyDeferredStorageSvc.saveAllDeferred();
    
    // 驗證載入成功
    runInTransaction(() -> {
      TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(ITermLoaderSvc.ICD10CM_URI);
      System.out.println("載入的 ICD-10-CM 版本: " + codeSystem.getCurrentVersion().getCodeSystemVersionId());
      System.out.println("載入的概念數量: " + myTermConceptDao.count());
    });

    // 等待CodeSystem處理完成 - 大型術語系統需要時間索引
    // System.out.println("等待CodeSystem處理完成...");
    // Thread.sleep(10000); // 等待10秒讓系統處理上傳的數據

    // 2. 讀入並建立 ValueSet
    String vsJson = ClasspathUtil.loadResource("/my-valueset.json");
    ValueSet vs = myFhirCtx.newJsonParser().parseResource(ValueSet.class, vsJson);

    // 檢查 ValueSet 的構成
    System.out.println("ValueSet URL: " + vs.getUrl());
    System.out.println("ValueSet compose includes:");
    vs.getCompose().getInclude().forEach(include -> {
      System.out.println("  System: " + include.getSystem() + " Version: " + include.getVersion());
      if (include.hasFilter()) {
        include.getFilter().forEach(filter -> {
          System.out.println("    Filter: " + filter.getProperty() + " " + filter.getOp() + " " + filter.getValue());
        });
      }
      include.getConcept().forEach(concept -> {
        System.out.println("    Code: " + concept.getCode() + " Display: " + concept.getDisplay());
      });
    });

    myClient.create().resource(vs).execute();

    // 3. 組裝輸入的 Parameters 並呼叫 $expand
    Parameters inParams = new Parameters();
    inParams.addParameter().setName("url").setValue(vs.getUrlElement());

    ValueSet expanded = null;
    int maxRetries = 3;
    int currentRetry = 0;
    
    while (expanded == null && currentRetry < maxRetries) {
      try {
        System.out.println("嘗試第 " + (currentRetry + 1) + " 次展開ValueSet...");
        expanded = myClient
            .operation()
            .onType(ValueSet.class)
            .named("$expand")
            .withParameters(inParams)
            .returnResourceType(ValueSet.class)
            .execute();
        System.out.println("ValueSet展開成功！");
      } catch (Exception e) {
        currentRetry++;
        System.out.println("展開失敗 (第 " + currentRetry + " 次嘗試): " + e.getMessage());
        if (currentRetry < maxRetries) {
          System.out.println("等待5秒後重試...");
          Thread.sleep(5000);
        } else {
          System.out.println("所有重試都失敗，拋出異常");
          throw e;
        }
      }
    }

    // 4. 印出所有 contains
    expanded.getExpansion().getContains().forEach(c -> {
      System.out.println(
          "system=" + c.getSystem() +
              " code=" + c.getCode() +
              " display=" + c.getDisplay());
    }); 

    System.out.println("Expansion successful! Found " + expanded.getExpansion().getContains().size() + " concepts.");

  }
}