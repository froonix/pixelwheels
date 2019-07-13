/*
 * Copyright 2019 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.agateau.ui.animscript;

import com.agateau.ui.DimensionParser;
import com.badlogic.gdx.utils.Array;

import java.io.StreamTokenizer;

public class ParallelInstructionDefinition implements InstructionDefinition {
    private AnimScriptLoader mLoader;

    ParallelInstructionDefinition(AnimScriptLoader loader) {
        mLoader = loader;
    }

    @Override
    public Instruction parse(StreamTokenizer tokenizer, DimensionParser dimParser) throws AnimScriptLoader.SyntaxException {
        Array<Instruction> lst = mLoader.tokenize(tokenizer, "end", dimParser);
        return new ParallelInstruction(lst);
    }
}
