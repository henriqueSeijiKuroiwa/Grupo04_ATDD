package com.facens.grupo04_atdd.aluno.domain;

import com.facens.grupo04_atdd.gamificacao.domain.Badge;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ideal para colunas SERIAL/BIGSERIAL no PostgreSQL
    private Long id;

    @Column(nullable=false)
    private String nome;

    private String plano = "BASICO";

    @Column(name = "cursos_concluidos")
    private int cursosConcluidos = 0;

    @Column(name = "creditos_cursos")
    private int creditosCursos = 3; // O aluno básico começa com créditos iniciais

    private int moedas = 0;

    @Column(name = "voucher_ativo")
    private boolean voucherAtivo = false;

    @Column(name = "pontos_ranking")
    private int pontosRanking = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_aluno_badge",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private Set<Badge> badges = new HashSet<>();

    protected Aluno() {}

    public Aluno(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }

    public int getCursosConcluidos() { return cursosConcluidos; }
    public void setCursosConcluidos(int cursosConcluidos) { this.cursosConcluidos = cursosConcluidos; }

    public int getCreditosCursos() { return creditosCursos; }
    public void setCreditosCursos(int creditosCursos) { this.creditosCursos = creditosCursos; }

    public int getMoedas() { return moedas; }
    public void setMoedas(int moedas) { this.moedas = moedas; }

    public int getPontosRanking() { return pontosRanking; }
    public void setPontosRanking(int pontosRanking) { this.pontosRanking = pontosRanking; }
    public Set<Badge> getBadges() { return badges; }

    public void adicionarPontos(int pontos) { this.pontosRanking += pontos; }
    public void adicionarBadge(Badge badge) { this.badges.add(badge); }

    public boolean isVoucherAtivo() { return voucherAtivo; }
    public void setVoucherAtivo(boolean voucherAtivo) { this.voucherAtivo = voucherAtivo; }

    // Métodos de negócio auxiliares
    public void incrementarCursosConcluidos() { this.cursosConcluidos++; }
    public void adicionarCreditosCursos(int quantidade) { this.creditosCursos += quantidade; }
    public void adicionarMoedas(int quantidade) { this.moedas += quantidade; }


}