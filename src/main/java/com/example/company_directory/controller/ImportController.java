package com.example.company_directory.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.company_directory.dto.ImportResultDto;
import com.example.company_directory.service.ExcelImportService;

@Controller
public class ImportController {

    private final ExcelImportService excelImportService;

    public ImportController(ExcelImportService excelImportService){
        this.excelImportService=excelImportService;
    }
    
    @GetMapping("/import")
    public String upload() {
        return "companies/import";
    }

    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            //ファイルが空の場合
            if (file == null || file.isEmpty()) {
                model.addAttribute("errorMessage", "ファイルが選択されていません。");
                return "companies/import";
            }
            //ファイル名、拡張子が正しくない場合
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".xlsx")) {
                model.addAttribute("errorMessage", "Excelファイル(.xlsx)のみアップロード可能です。");
                return "companies/import";
            }

            // ファイルを一時フォルダに保存する
            // ファイル名が被らないようにUUIDを使う
            String tempFileName = UUID.randomUUID().toString() + ".xlsx";
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")); // OSの一時フォルダ
            Path tempFilePath = tempDir.resolve(tempFileName);
            
            // ファイルをコピー配置
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Excel を解析して結果を model に詰める
            ImportResultDto result = excelImportService.importExcel(file.getInputStream());
            model.addAttribute("importResult", result);
            model.addAttribute("tempFileName", tempFileName);

            // 同じ画面に戻してプレビューを表示
            return "companies/import";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "アップロードしたファイルの読み込みに失敗しました。");
            return "companies/import";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "companies/import";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "予期しないエラーが発生しました。");
            return "companies/import";
        }
    }

    // ★追加: 確定登録実行
    @PostMapping("/import/execute")
    public String executeImport(@RequestParam("tempFileName") String tempFileName, Model model) {
        try {
            // 1. 一時ファイルのパスを取得
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path tempFilePath = tempDir.resolve(tempFileName);

            // ファイルが存在するかチェック
            if (!Files.exists(tempFilePath)) {
                model.addAttribute("errorMessage", "セッションが切れました。もう一度アップロードしてください。");
                return "companies/import";
            }

            // 2. 再度解析して、成功データだけを取り出す
            // (メモリ節約のため、ここで再解析するのが安全です)
            InputStream is = Files.newInputStream(tempFilePath);
            ImportResultDto result = excelImportService.importExcel(is);

            // 3. DB保存実行
            //登録できるデータがない場合は、エラー行をすべて見せる仕様に後で修正
            if (result.getSuccessList().isEmpty()) {
                model.addAttribute("errorMessage", "登録できるデータがありませんでした。");
                return "companies/import";
            }
            
            excelImportService.saveValidData(result.getSuccessList());

            // 4. 後始末（一時ファイルを削除）
            Files.deleteIfExists(tempFilePath);

            // 5. 完了画面（または一覧）へリダイレクト
            // フラッシュメッセージ（保存しました！）を出したい場合は RedirectAttributes を使います
            return "redirect:/companies";

            //後でエラー処理を追加
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "登録処理中にエラーが発生しました。");
            return "companies/import";
        }
    }


}
