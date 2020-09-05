package br.com.projeto.jpa.padrao.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.projeto.jpa.padrao.entity.Aluno;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AlunoRepositoryTest {
	
	@Autowired
	private AlunoRepository repository;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void createShouldPersistData() {
		Aluno a1 = new Aluno(1L, "Maria Clara", 5);
		Aluno a2 = new Aluno(2L, "Maria Clara", 5);
		this.repository.save(a1);
		this.repository.save(a2);
		List<Aluno> all = this.repository.findAll();
		Assertions.assertThat(all.size()).isEqualTo(2);
	}

	
	
	
}
