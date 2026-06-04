package com.example.lgauthservice.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static Path combineNasPath(String rootPath, String... paths) {
		var firstStr = paths[0];
		var firstPath = Paths.get(firstStr);
		if (firstPath.startsWith(Paths.get(rootPath)))
			return Paths.get("", paths);
		return Paths.get(rootPath, paths);
	}

	public static boolean isExistPath(Path path) {
		File dir = new File(path.toUri());
		return dir.exists();
	}

//	private static String[] dateToPathYYYDDMM(Date date) {
//		String year = DateUtil.getStringDateByFormat(date, "yyyy");
//		String month = DateUtil.getStringDateByFormat(date, "MM");
//		String day = DateUtil.getStringDateByFormat(date, "dd");
//		return new String[] { year, month, day };
//	}

//	public static Path getNasFolderPathByDate(String rootPath, Date date) {
//		var paths = dateToPathYYYDDMM(date);
//		return tryGetNasPathOrCreate(rootPath, paths);
//	}

//	public static Path getNasFolderToday(String rootPath) {
//		return getNasFolderPathByDate(rootPath, new Date());
//	}

//	@UseLogging(isLogBefore = true)
	public static Path tryGetNasPathOrCreate(String rootPath, String... paths) {
		Path path = combineNasPath(rootPath, paths);

		if (isExistPath(path)) {
			return path;
		}
		File dir = new File(path.toUri());
		var isCreated = dir.mkdirs();
		if (isCreated)
			return path;
		return null;
	}

//	@UseLogging(isLogBefore = true)
//	public static Path getNasFilePathByDate(String rootPath, String fileName, Date date) {
//		var datePaths = dateToPathYYYDDMM(date);
//
//		var listArrayCombined = new ArrayList<>(Arrays.asList(datePaths));
//		listArrayCombined.add(fileName);
//
//		var listArrayCombinedArray = listArrayCombined.toArray(new String[0]);
//		Path path = combineNasPath(rootPath, listArrayCombinedArray);
//		if (isExistPath(path)) {
//			return combineNasPath(rootPath, listArrayCombinedArray);
//		}
//		return null;
//	}

//	public static Path getNasFilePathToday(String rootPath, String fileName) {
//		return getNasFilePathByDate(rootPath, fileName, new Date());
//	}

//	@UseLogging
	public static Path saveFileToPath(Path path, byte[] content) {
		try {
            return Files.write(path, content);
		} catch (Exception ex) {
            logger.error("saveFileToPath Error: {}", ex.getMessage(), ex);
			return null;
		}
	}

//	@UseLogging
//	public static Path saveFileToFolderToday(String rootPath, String fileName, byte[] content) {
//		var nasFolderToday = getNasFolderToday(rootPath);
//		var newPath = Paths.get(String.valueOf(nasFolderToday), fileName);
//		try {
//			var savedPath = Files.write(newPath, content);
//			return savedPath;
//		} catch (Exception ex) {
//			logger.error("saveFileToFolderToday Error: " + ex.getMessage(), ex);
//			return null;
//		}
//	}
//
////	@UseLogging
//	public static Path saveFileFolderToday(String rootPath, String fileName, String content) {
//		var nasFolderToday = getNasFolderToday(rootPath);
//		var newPath = Paths.get(String.valueOf(nasFolderToday), fileName);
//		try {
//			FileWriter myWriter = new FileWriter(newPath.toString());
//			myWriter.write(content);
//			myWriter.close();
//			return newPath;
//		} catch (IOException ex) {
//			logger.error("saveFileFolderToday Error: " + ex.getMessage(), ex);
//			return null;
//		}
//	}
//
////	@UseLogging
//	public static Path saveFileFolderToday(String rootPath, MultipartFile file) {
//		var nasFolderToday = getNasFolderToday(rootPath);
//		var newPath = Paths.get(String.valueOf(nasFolderToday), file.getOriginalFilename());
//		try {
//			file.transferTo(newPath);
//			return newPath;
//		} catch (IOException ex) {
//			logger.error("saveFileFolderToday1 Error: " + ex.getMessage(), ex);
//			return null;
//		}
//	}

//	@UseLogging(isLogBefore = true, isLogAfter = false)
	public static byte[] getContentFile(Path path) {
		if (path != null) {
			var isExistPath = isExistPath(path);
			if (isExistPath) {
				try {
					var file = new File(path.toUri());
                    return Files.readAllBytes(file.toPath());
				} catch (Exception ex) {
                    logger.error("getContentFile Error: {}", ex.getMessage(), ex);
					return null;
				}
			}else {
				logger.error("getContentFile Error File không tồn tại vs path: " + path);
			}
		}
		return null;
	}

//	public static <T> T readResourceFileToObject(String pathFile, Class<T> clazz) {
//		String text = getResourceFileAsString(pathFile, FileUtil.class);
//		return JsonConvertUtil.convertStringToObjectComplex(text, clazz);
//	}

	public static String getResourceFileAsString(String fileName, Class<?> currentClass) throws FileNotFoundException {
		InputStream is = getResourceFileAsInputStream(fileName, currentClass);
		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			return (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} else {
			throw new RuntimeException("resource not found");
		}
	}

	public static InputStream getResourceFileAsInputStream(String fileName, Class<?> currentClass) throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:" + fileName);
        if (file.exists()) {
            return new FileInputStream(file);
        }
		ClassLoader classLoader = currentClass.getClassLoader();
		return classLoader.getResourceAsStream(fileName);
	}
}
