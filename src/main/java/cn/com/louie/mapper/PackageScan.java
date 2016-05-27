package cn.com.louie.mapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Louie on 2016/5/24.
 */
public class PackageScan {
    private String basePackage;
    private ClassLoader cl;
    private String matchUrl = "";

    public PackageScan(String basePackage) {
        matchUrl = "^" + basePackage.replaceAll("\\.", "\\.").replace("*", "(\\S+)") + "((\\.\\S+)+)";
        if (basePackage.contains("*")) {
            basePackage = basePackage.split("\\*")[0];
            if (basePackage.endsWith("."))
                basePackage = basePackage.substring(0, basePackage.length() - 1);
            this.basePackage = basePackage;
        }
        this.cl = getClass().getClassLoader();

    }


    public PackageScan(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }


    public List<String> getFullyQualifiedClassNameList() throws IOException {
        return doScan(basePackage, new ArrayList<String>());
    }

    private List<String> doScan(String packageName, List<String> nameList) throws IOException {
        String splashPath = this.dotToSplash(packageName);
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageName);
        while (dirs.hasMoreElements()) {
            forScan(dirs.nextElement(), splashPath, nameList, packageName);
        }
        return nameList;
    }

    private List<String> doChildScan(String packageName, List<String> nameList) throws IOException {
        String splashPath = this.dotToSplash(packageName);
        URL url = cl.getResource(splashPath);
        forScan(url, splashPath, nameList, packageName);
        return nameList;
    }

    public List<String> forScan(URL url, String splashPath, List<String> nameList, String packageName) throws IOException {
        String filePath = this.getRootPath(url);
        List<String> names = null;
        if (isJarFile(filePath)) {
            try {
                names = readFromJarFile(filePath, splashPath);
            } catch (IOException e) {
                names = new ArrayList<String>();
            }

        } else {
            names = readFromDirectory(filePath);
        }

        for (String name : names) {
            if (isClassFile(name)) {
                String className = toFullyQualifiedName(name, packageName);
                if (className.matches(matchUrl))
                    nameList.add(className);
            } else {
                doChildScan(packageName + "." + name, nameList);
            }
        }
        return nameList;
    }

    private String toFullyQualifiedName(String shortName, String packageName) {
        StringBuilder sb = new StringBuilder(packageName);
        sb.append('.');
        sb.append(this.trimExtension(shortName));
        String className = sb.toString();
        if (className.matches(matchUrl))
            return className;
        return className;
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {

        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<String>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                if (!name.matches(matchUrl))
                    nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return null;
        }

        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }


    public static String getRootPath(URL url) {

        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }


    public static String dotToSplash(String name) {
        return name.replaceAll("\\.", "/");
    }


    public String trimExtension(String name) {
        if (isClassFile(name)) {
            name = name.replaceAll("/", ".").replaceAll(basePackage, "");
            if (name.startsWith("."))
                return name.substring(1, name.lastIndexOf("."));
            return name.substring(0, name.lastIndexOf("."));
        }
        return name;
    }

    public static String trimURI(String uri) {
        String trimmed = uri.substring(1);
        int splashIndex = trimmed.indexOf('/');
        return trimmed.substring(splashIndex);
    }

}
