package javaeetutorial.hello;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/webapi")
public class MyApplication extends Application{
	
	@Override
	public Set<Class<?>> getClasses(){
		final Set<Class<?>> classes = new HashSet<>();
		classes.add(HelloWorld.class);
		
		return classes;
		
	}

}
