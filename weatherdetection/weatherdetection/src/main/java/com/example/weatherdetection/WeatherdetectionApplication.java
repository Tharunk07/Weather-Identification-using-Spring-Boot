package com.example.weatherdetection;

import org.apache.catalina.connector.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

@SpringBootApplication
public class WeatherdetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherdetectionApplication.class, args);
	}

}
//This is the Controller Application
@Controller
class MyController {

	@PostMapping("/submit/")
	public ResponseEntity<String> ansform(@RequestParam("location") String location) {
		try {
			URI uri = URI
					.create("http://api.openweathermap.org/data/2.5/weather?appid=147b1134e5c0d89525cc02377dc0940f&q="
							+ location);
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				// Parse JSON using Gson
				Gson gson = new Gson();
				Map<String, Object> weatherData = gson.fromJson(response.toString(), Map.class);
				// System.out.println(weatherData);
				Map<String, Object> map2 = (Map<String, Object>) weatherData.get("main");
				List<Map<String, Object>> map3 = (List<Map<String, Object>>) weatherData.get("weather");
				// System.out.println(map2);
				double temp = (double) map2.get("temp");
				double temperature = temp - 273;
				double humidity = (double) map2.get("humidity");
				String weather = (String) map3.get(0).get("main");
				// System.out.println(weather);
				// System.out.println(humidity);
				// System.out.println(temperature);
				String ans = "Weather :" + weather + "\n" + "Humidity :" + humidity + "\n" + "Temperature :"
						+ temperature;
				return ResponseEntity.ok(ans);
			} else {
				return ResponseEntity.ok("Unexpected Error!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("An error occurred");
		}
	}
}
