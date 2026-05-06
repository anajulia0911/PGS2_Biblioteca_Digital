package br.mackenzie.bibliotecamack.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date dataEmprestimo;
    
    @Temporal(TemporalType.DATE)
    private Date dataDevolucaoPrevista;
    
    private int status; 

    @ManyToOne
    private Leitor leitor;

    @ManyToMany
    @JoinTable(
        name = "emprestimo_livros",
        joinColumns = @JoinColumn(name = "emprestimo_id"),
        inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    private List<Livro> livros;

    //GETTERS E SETTERS

    public Long getId() { 
        return id; }

    public void setId(Long id) { 
        this.id = id; }

    public Date getDataEmprestimo() { 
        return dataEmprestimo; }

    public void setDataEmprestimo(Date dataEmprestimo) { 
        this.dataEmprestimo = dataEmprestimo; }

    public Date getDataDevolucaoPrevista() { 
        return dataDevolucaoPrevista; }

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) { 
        this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public int getStatus() { 
        return status; }

    public void setStatus(int status) { 
        this.status = status; }

    public Leitor getLeitor() { 
        return leitor; }

    public void setLeitor(Leitor leitor) { 
        this.leitor = leitor; }

    public List<Livro> getLivros() { 
        return livros; }

    public void setLivros(List<Livro> livros) { 
        this.livros = livros; }
}