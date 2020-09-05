package br.com.projeto.jpa.padrao.rest;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.projeto.jpa.padrao.entity.Aluno;
import br.com.projeto.jpa.padrao.repository.AlunoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //only if you will use @MockMvc
@ActiveProfiles("test")
public class AlunoControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort //only for port
	private int port;
	
	@MockBean
	private AlunoRepository repository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@TestConfiguration
	static class config {
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthentication("sa", "");
		}
	}
	
    @Before
    public void setup() {
        //Aluno aluno = new Aluno(1L, "Legolas", 7);
        //BDDMockito.when(repository.findById(aluno.getId())).thenReturn(Optional<Aluno>);
    }
    
    //USANDO REST_TEMPLATE - como construtor da request:
    
	@Test
	public void getAlunosAndReturningStatus200() {
		Aluno a1 = new Aluno(1L, "Maria Clara", 5);
		Aluno a2 = new Aluno(2L, "Maria Clara", 5);
		List<Aluno> lista = new ArrayList<>();
		lista.add(a1);
		lista.add(a2);
		BDDMockito.when(repository.findAll()).thenReturn(lista);
		ResponseEntity<String> response = restTemplate.getForEntity("/alunos/get", String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getApenasUmAlunoStatus200() {
		Aluno a1 = new Aluno(1L, "Maria Clara", 5);
		BDDMockito.when(repository.getOne(a1.getId())).thenReturn(a1);
		ResponseEntity<Aluno> response = restTemplate.getForEntity("/alunos/get/{id}", Aluno.class, a1.getId());
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void getApenasUmAlunoINcorretoStatus404() {
		ResponseEntity<Aluno> response = restTemplate.getForEntity("/alunos/get/{id}", Aluno.class, -1);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
		
	@Test
	public void gravarAlunosAndReturningStatus201() {
		Aluno aluno = new Aluno(1L, "Maria Clara", 5);
		BDDMockito.when(repository.save(aluno)).thenReturn(aluno);
		ResponseEntity<Aluno> response = restTemplate.postForEntity("/alunos/post", aluno, Aluno.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
	}
	
	@Test
	public void deleteAluno200() {
		Aluno aluno = new Aluno(1L, "Maria Clara", 5);
		
		BDDMockito.when(repository.getOne(aluno.getId())).thenReturn(aluno);

		BDDMockito.doNothing().when(repository).deleteById(1L);
		
		ResponseEntity<String> response = restTemplate.exchange("/alunos/delete/{id}", HttpMethod.DELETE, null, String.class, 1L);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void deleteAluno404() {
		ResponseEntity<String> response = restTemplate.exchange("/alunos/delete/{id}", HttpMethod.DELETE, null, String.class, -1);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	//USANDO MOCK_MVC - como construtor da request:
	
	@Test
	public void deleteAluno_MOckMvc404() throws Exception {
		BDDMockito.doNothing().when(repository).deleteById(1L);
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/alunos/delete/{id}", 1L))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void deleteAluno_MOckMvc200() throws Exception {
		Aluno aluno = new Aluno(1L, "Maria Clara", 5);
		BDDMockito.when(repository.getOne(aluno.getId())).thenReturn(aluno);
		BDDMockito.doNothing().when(repository).deleteById(1L);
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/alunos/delete/{id}", 1L))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void gravarAlunosAndReturningStatus_MockMvc_201() throws Exception {
		Aluno aluno = new Aluno(1L, "Maria Clara", 5);
		BDDMockito.when(repository.save(aluno)).thenReturn(aluno);

		ObjectMapper obj = new ObjectMapper();
		String jsonObj = obj.writeValueAsString(aluno);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/alunos/post", jsonObj)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		
	}
	
	
	
	
	
	
	
	
	
}
