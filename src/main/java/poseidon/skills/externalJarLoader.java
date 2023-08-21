package poseidon.skills;

import poseidon.skills.JSON.JSONSave;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.jar.JarFile;
@Deprecated
public class externalJarLoader {
    private static HashMap<String, Method> methodMap = new HashMap<>();
    public static Method getMethode(String name){
        return methodMap.get(name);
    }

    public static void loadClasses() {
        File a = new File(getClassPath());
        if(!a.mkdirs()){
            for(File f : Objects.requireNonNull(a.listFiles())){
               getMethodsFromJar(f.getPath());
            }
        }
    }

    public static String getClassPath(){
        String a = JSONSave.getPluginpath();
        String dic1Path = Paths.get(a).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/SkillJars/";
    }

    public static void getMethodsFromJar(String jarFilePath) {
        try {
            File jarFile = new File(jarFilePath);

            if (!jarFile.exists()) {
                System.out.println("Die JAR-Datei wurde nicht gefunden: " + jarFilePath);
                return;
            }

            URL jarUrl = jarFile.toURI().toURL();
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { jarUrl });
            JarFile jar = new JarFile(jarFile);

            jar.stream().forEach(jarEntry -> {
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);

                    try {
                        Class<?> cls = classLoader.loadClass(className);
                        Method[] methods = cls.getDeclaredMethods();
                        for (Method method : methods) {
                            methodMap.put(method.getName(), cls.getDeclaredMethod(method.getName(), method.getParameterTypes()));
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError e) {
                        System.out.println("Fehler beim Laden der Klasse: " + className);
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            jar.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Öffnen der JAR-Datei: " + jarFilePath);
            e.printStackTrace();
        }
    }
}
