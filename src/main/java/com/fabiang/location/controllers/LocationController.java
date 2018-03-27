package com.fabiang.location.controllers;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fabiang.location.entities.Location;
import com.fabiang.location.service.LocationService;
import com.fabiang.location.util.EmailUtil;
import com.fabiang.location.util.ReportUtil;

@Controller
public class LocationController {
	
	@Autowired
	LocationService service;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Autowired
	ReportUtil reportUtil;
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(	"/showCreate")
	public String showCreate(@ModelAttribute("location") Location location) {
		return "createLocation";
	}
	
	@RequestMapping(value="/saveLocation", method=RequestMethod.POST)
	public String saveLocation(@ModelAttribute("location") Location location, ModelMap modelMap) {
		Location locationSaved = service.saveLocation(location);
		String msg = "Location saved with id: "+locationSaved.getId();
		modelMap.addAttribute("msg", msg);
		emailUtil.sendEmail("fabian81tester@gmail.com", "Location Saved", "Location saved successfully and about to return a response");
		return "createLocation";
	}
	
	@RequestMapping("/displayLocations")
	public String displayLocations(ModelMap modelMap) {
		List<Location> locations = service.getAllLocations();
		modelMap.addAttribute("locations", locations);
		return "displayLocations";
	}
	
	@RequestMapping("/deleteLocation")
	public String deleteLocation(@RequestParam("id") int id, ModelMap modelMap) {
		Location location = service.getLocationById(id);
		service.deleteLocation(location);
		List<Location> locations = service.getAllLocations();
		String msg = "Location deleted with id: "+location.getId();
		modelMap.addAttribute("locations", locations);
		modelMap.addAttribute("msg", msg);
		return "displayLocations";
	}
	
	@RequestMapping("/updateLocation")
	public String updateLocation(@RequestParam("id") int id, ModelMap modelMap) {
		Location location = service.getLocationById(id);
		modelMap.addAttribute("location", location);
		return "updateLocation";
	}
	
	@RequestMapping("/showUpdate")
	public String showUpdate(@ModelAttribute("location") Location location, ModelMap modelMap) {
		service.updateLocation(location);
		List<Location> locations = service.getAllLocations();
		modelMap.addAttribute("locations", locations);
		String msg = "Location updated with id: "+location.getId();
		modelMap.addAttribute("msg", msg);
		return "displayLocations";
	}
	
	@RequestMapping("/generateReport")
	public String generateReport() {
		String path = context.getRealPath("/");
		List<Object[]> data = service.findTypeAndTypeCount();
		reportUtil.generatePieChart(path, data);
		return "report";
	}

}
