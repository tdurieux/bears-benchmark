/**
 * H2MS API
 * API for interacting with the H2MS backend.
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import { Answer } from './answer';
import { EventTemplate } from './eventTemplate';
import { Link } from './link';
import { User } from './user';


export interface ResourceEvent {
    answers?: Array<Answer>;
    eventTemplate?: EventTemplate;
    id?: number;
    links?: Array<Link>;
    location?: string;
    observer?: User;
    subject?: User;
    timestamp?: Date;
}
