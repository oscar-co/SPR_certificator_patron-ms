workspace 1 "Certificator Workspace" {

    !identifiers hierarchical

    model {
        u = person "User"

        ss = softwareSystem "Certificator" {
    
            group "Client Tier" {
                wf = container "Frontend Certificator" "Web UI for users" "Angular"
            }

            group "Server Tier" {
                wb = container "Backend Certificator" "REST API and business logic" "Spring Boot"
                db = container "Database PostgreSQL" "Stores certifications and users" "PostgreSQL" {
                    tags "Database"
                }
            }
        }

        u -> ss.wf "Uses"

        ss.wf -> ss.wb "Sends API requests with JWT for auth" "HTTPS + JSON + JWT"
        ss.wb -> ss.db "Reads from and writes to" "JPA over JDBC"
    }

    views {
        systemContext ss "Diagram1" {
            include *
            autolayout lr
        }

        container ss "Diagram2" {
            include *
            autolayout lr
        }

        styles {
            element "Element" {
                color #0773af
                stroke #0773af
                strokeWidth 7
                shape roundedbox
            }
            element "Person" {
                shape person
            }
            element "Frontend" {
                background #e3f2fd
                shape roundedbox
            }
            element "Backend" {
                background #e8f5e9
                shape roundedbox
            }
            element "Database" {
                shape cylinder
                background #fbe9e7
            }
            relationship "Relationship" {
                thickness 4
            }
        }
    }
}
