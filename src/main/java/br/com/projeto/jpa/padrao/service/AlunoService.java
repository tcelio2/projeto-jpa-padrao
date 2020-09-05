package br.com.projeto.jpa.padrao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.jpa.padrao.entity.Aluno;
import br.com.projeto.jpa.padrao.repository.AlunoRepository;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository repository;
	
	public List<Aluno> obterAlunosService() {
		return repository.findAll();
	}

	public Aluno save(Aluno aluno) {
		return repository.save(aluno);
	}

	public Aluno obterOne(Long id) {
		return repository.getOne(id);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
}
