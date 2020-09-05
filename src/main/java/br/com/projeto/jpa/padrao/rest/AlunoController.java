package br.com.projeto.jpa.padrao.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.jpa.padrao.entity.Aluno;
import br.com.projeto.jpa.padrao.exception.ResourceNotFoundException;
import br.com.projeto.jpa.padrao.service.AlunoService;

@RestController
@RequestMapping("alunos")
public class AlunoController {

	@Autowired
	private AlunoService service;
	
	@GetMapping("/get")
	public ResponseEntity<List<Aluno>> obterAlunos() {
		return ResponseEntity.ok(service.obterAlunosService());
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> obterOneAlunos(@PathVariable("id") Long id) {
		 verifyIfStudentExists(id);
		return new ResponseEntity<>(service.obterOne(id), HttpStatus.OK);
	}

	@PostMapping(path="/post", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> criarAlunos(Aluno aluno) {
      
		return new ResponseEntity<>(service.save(aluno), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path="/delete/{id}")
	public ResponseEntity<?> deleteAluno(@PathVariable("id") Long id) {
		verifyIfStudentExists(id);
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void verifyIfStudentExists(Long id) {
		if (service.obterOne(id) == null)
			throw new ResourceNotFoundException("Student not found for ID: "+id);		
	}
}
