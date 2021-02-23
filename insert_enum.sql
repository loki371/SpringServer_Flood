insert into roles values (1, 'ROLE_USER'),
                         (2, 'ROLE_AUTHORITY'),
                         (3, 'ROLE_RESCUER'),
                         (4, 'ROLE_VOLUNTEER');

insert into comment_types values (1, 'SAFE'),
                                 (2, 'WARNING'),
                                 (3, 'DANGER');

insert into states values (1, 'STATE_UNAUTHEN'),
                          (2, 'STATE_SAFE'),
                          (3, 'STATE_DANGER'),
                          (4, 'STATE_EMERGENCY'),
                          (5, 'STATE_SAVED');