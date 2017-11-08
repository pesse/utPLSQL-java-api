package org.utplsql.api;

import org.utplsql.api.reporter.Reporter;

import java.util.ArrayList;
import java.util.List;

public class TestRunnerOptions {
    public List<String> pathList = new ArrayList<>();
    public List<Reporter> reporterList = new ArrayList<>();
    public boolean colorConsole = false;
    public List<String> coverageSchemes = new ArrayList<>();
    public List<String> sourceFiles = new ArrayList<>();
    public List<String> testFiles = new ArrayList<>();
    public List<String> includeObjects = new ArrayList<>();
    public List<String> excludeObjects = new ArrayList<>();
    public FileMapperOptions sourceMappingOptions;
    public FileMapperOptions testMappingOptions;
    public boolean failOnErrors = false;
}