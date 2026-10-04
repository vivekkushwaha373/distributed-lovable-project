package com.agenticIde.distributed_lovable.intelligence_service.entity;


import com.agenticIde.distributed_lovable.comman_lib.enums.MessageRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name="project_id", referencedColumnName = "projectId",nullable = false),
            @JoinColumn(name="user_id",referencedColumnName = "userId",nullable = false)
    })
    ChatSession chatSession;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageRole role;

    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sequenceOrder ASC")
    List<ChatEvent> events;

    @Column(columnDefinition = "text",nullable = false)
    String content;
    String toolCalls;
    Integer tokenUsed = 0;
    @CreationTimestamp
    Instant createdAt;
}
