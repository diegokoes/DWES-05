@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> users;

    public Rol() {
        users = new HashSet<Usuario>();
    }

    public Rol(Long id, String name) {
        this();
        this.id = id;
        this.name = name;

    }

    @Override
    public String toString() {
        return "Rol{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
