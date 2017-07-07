package servlets;

import javax.servlet.ServletContextEvent;

import it.unipi.ing.mim.deep.DNNExtractor;

public class Listener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		DNNExtractor.getInstance();
	}
	}