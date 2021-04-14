/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;

public class JSContact2VCardTest {

    final JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder().build()).build();

}
