package com.company.support.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.company.handler.CursoHandler;
import com.company.handler.EstudianteHandler;
import com.company.handler.MatriculaHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> rutasCursos(CursoHandler handler) {
		// req -> handler.getAll(req)
		return route(GET("/v1/cursos"), handler::getAll)
				.andRoute(GET("/v1/cursos/{id}"), handler::getById)
				.andRoute(POST("/v1/cursos"), handler::insert)
				.andRoute(PUT("/v1/cursos/{id}"), handler::update)
				.andRoute(DELETE("/v1/cursos/{id}"), handler::delete);
	}
	
	@Bean
	public RouterFunction<ServerResponse> rutasEstudiantes(EstudianteHandler handler) {
		// req -> handler.getAll(req)
		return route(GET("/v1/estudiantes"), handler::getAll)
				.andRoute(GET("/v1/estudiantes/{id}"), handler::getById)
				.andRoute(POST("/v1/estudiantes"), handler::insert)
				.andRoute(PUT("/v1/estudiantes/{id}"), handler::update)
				.andRoute(DELETE("/v1/estudiantes/{id}"), handler::delete);
	}
	
	@Bean
	public RouterFunction<ServerResponse> rutasMatriculas(MatriculaHandler handler) {
		// req -> handler.getAll(req)
		return route(GET("/v1/matriculas"), handler::getAll)
				.andRoute(GET("/v1/matriculas/{id}"), handler::getById)
				.andRoute(POST("/v1/matriculas"), handler::insert)
				.andRoute(PUT("/v1/matriculas/{id}"), handler::update)
				.andRoute(DELETE("/v1/matriculas/{id}"), handler::delete);
	}

}
