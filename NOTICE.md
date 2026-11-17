# Third-party components bundled in AI ANC Phone

This file lists third-party code bundled in AI ANC Phone (compiled into
`libaianc.so` or pulled in as Android dependencies) together with their
copyright and license notices. The project's own code is licensed under the
MIT License (see `LICENSE`).

License texts below were extracted verbatim from the sources at tag v0.1.1
(RNNoise) and from the vendored file headers. No text was quoted from memory.

---

## RNNoise

- Project: RNNoise — https://github.com/xiph/rnnoise (convenience copy of
  https://gitlab.xiph.org/xiph/rnnoise)
- Version: v0.1.1
- Copyright: Copyright (c) 2017, Mozilla; Copyright (c) 2007-2017, Jean-Marc Valin;
  Copyright (c) 2005-2017, Xiph.Org Foundation; Copyright (c) 2003-2004, Mark Borgerding
- License: BSD 3-clause (see `COPYING` in the RNNoise source tree)

License text (from `COPYING`):

```
Copyright (c) 2017, Mozilla
Copyright (c) 2007-2017, Jean-Marc Valin
Copyright (c) 2005-2017, Xiph.Org Foundation
Copyright (c) 2003-2004, Mark Borgerding

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

- Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

- Neither the name of the Xiph.Org Foundation nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE FOUNDATION
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

---

## Kiss FFT

RNNoise vendors a modified copy of Kiss FFT. The vendored files are
`src/kiss_fft.h` and `src/kiss_fft.c`.

- Author (per source header): Mark Borgerding
- Copyright: Copyright (c) 2003-2004, Mark Borgerding; Lots of modifications by
  Jean-Marc Valin; Copyright (c) 2005-2007, Xiph.Org Foundation;
  Copyright (c) 2008, Xiph.Org Foundation, CSIRO
- Source URL (from the original Kiss FFT source header): https://github.com/mborgerding/kissfft
- License: BSD (as reproduced in the vendored file header)

License text (verbatim from `src/kiss_fft.h` / `src/kiss_fft.c` header):

```
/*Copyright (c) 2003-2004, Mark Borgerding
  Lots of modifications by Jean-Marc Valin
  Copyright (c) 2005-2007, Xiph.Org Foundation
  Copyright (c) 2008,      Xiph.Org Foundation, CSIRO

  All rights reserved.

  Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
       this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
       this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
  POSSIBILITY OF SUCH DAMAGE.*/
```

Attribution request (verbatim comment in `src/kiss_fft.c`):

```
/* This code is originally from Mark Borgerding's KISS-FFT but has been
   heavily modified to better suit Opus */
```

---

## CELT-derived code

RNNoise distributes CELT-derived code as part of its own source tree
(`src/celt_lpc.c`, `src/celt_lpc.h`, `src/pitch.c`, `src/pitch.h`). It is
part of the RNNoise distribution and shares the Xiph BSD lineage above.

- Copyright: Copyright (c) 2009-2010, Xiph.Org Foundation (celt_lpc.c/h);
  Copyright (c) 2007-2008, CSIRO; Copyright (c) 2007-2009, Xiph.Org Foundation (pitch.c/h)
- License: BSD (2-clause style, as reproduced in the file headers)

License text (verbatim from `src/celt_lpc.c` header):

```
/* Copyright (c) 2009-2010 Xiph.Org Foundation
   Written by Jean-Marc Valin */
/*
   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:

   - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

   - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
   OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

---

## AndroidX / Material Components / ConstraintLayout / CardView

Android dependencies pulled in via Gradle (`app/build.gradle`):

- `androidx.core:core-ktx`, `androidx.appcompat:appcompat`,
  `androidx.constraintlayout:constraintlayout`, `androidx.cardview:cardview`,
  `com.google.android.material:material`
- License: Apache License, Version 2.0

Standard Apache-2.0 attribution:

> Licensed under the Apache License, Version 2.0 (the "License"); you may not
> use this file except in compliance with the License. You may obtain a copy of
> the License at https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.

- AndroidX: https://developer.android.com/jetpack/androidx
- Material Components for Android: https://github.com/material-components/material-components-android
- ConstraintLayout: https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout
- CardView: https://developer.android.com/reference/androidx/cardview/widget/CardView
