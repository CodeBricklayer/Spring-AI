package com.tp.springai.aimedicalpartner.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MedicalAppDocumentLoaderTest {

    @Resource
    private MedicalAppDocumentLoader medicalAppDocumentLoader;

    @Test
    void loadMarkdowns() {
        medicalAppDocumentLoader.loadMarkdowns();
    }

    @Test
    void loadJson() {
        System.out.println(medicalAppDocumentLoader.loadJson());
    }
}