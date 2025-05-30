package com.cqrs.socialfeed.infrards.notification;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplateJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String locale;

    @Column(nullable = false)
    private boolean useYn;

    protected NotificationTemplateJpaEntity() {}

    public NotificationTemplateJpaEntity(Long id, String code, String content, String locale, boolean useYn) {
        this.id = id;
        this.code = code;
        this.content = content;
        this.locale = locale;
        this.useYn = useYn;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getContent() { return content; }
    public String getLocale() { return locale; }
    public boolean isUseYn() { return useYn; }
}
