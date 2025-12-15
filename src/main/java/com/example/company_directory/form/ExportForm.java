package com.example.company_directory.form;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExportForm extends CompanySearchForm {
    private String scope; // "ALL", "SEARCH", "SELECTION"
    private List<Integer> selectedIds;
    private List<String> columns;
    private String fileName;

    // Sort options
    private String sortBy = "companyId"; // Default
    private String sortOrder = "ASC"; // Default
}
